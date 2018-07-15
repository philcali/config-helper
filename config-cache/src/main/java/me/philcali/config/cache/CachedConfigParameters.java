package me.philcali.config.cache;

import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import me.philcali.config.api.IParameter;
import me.philcali.config.api.IParameters;
import me.philcali.config.cache.command.CacheCommandType;
import me.philcali.config.cache.command.ICacheCommandExecution;
import me.philcali.config.cache.event.CacheEventType;
import me.philcali.config.cache.event.ICacheEventPublisher;
import me.philcali.config.cache.update.CacheEvictionPolicy;

public class CachedConfigParameters implements IParameters, ICacheCommandExecution {
    private static final class CachedItem {
        private final Optional<IParameter> parameter;
        private final CacheEvictionPolicy evictionPolicy;
        private final long expiresIn;

        public CachedItem(final Optional<IParameter> parameter, final CacheUpdatePolicy updatePolicy) {
            this.parameter = parameter;
            this.evictionPolicy = updatePolicy.getEvictionPolicy();
            this.expiresIn = System.currentTimeMillis() + (updatePolicy.getTimeUnit().toMillis(updatePolicy.getAmount()));
        }

        public boolean isAlive() {
            return evictionPolicy == CacheEvictionPolicy.NEVER
                    || expiresIn < System.currentTimeMillis();
        }

        public Optional<IParameter> getParameter() {
            return parameter;
        }
    }

    private final IParameters parameters;
    private final ICacheEventPublisher events;
    private final ICacheUpdateStrategy updateStrategy;
    private final Map<String, CachedItem> cache;

    public CachedConfigParameters(
            final IParameters parameters,
            final ICacheEventPublisher events,
            final ICacheUpdateStrategy updateStrategy) {
        this.parameters = parameters;
        this.events = events;
        this.updateStrategy = updateStrategy;
        this.cache = new ConcurrentHashMap<>();
    }

    public CachedConfigParameters initialize(final CacheInitializationPolicy initializationPolicy) {
        switch (initializationPolicy) {
        case FILL:
            parameters.getParameters().forEach(parameter -> {
                cache.computeIfAbsent(createCacheKey(parameter.getName()), key -> {
                    final Optional<IParameter> param = Optional.ofNullable(parameter);
                    events.publish(CacheEventType.CREATED, this, parameter);
                    return new CachedItem(param, updateStrategy.getUpdatePolicy());
                });
            });
            break;
        default:
            // do nothing ... it's lazy
        }
        return this;
    }

    private String createCacheKey(final String name) {
        return "CACHE." + String.join(".", parameters.getGroupName()) + "." + name;
    }

    @Override
    public Optional<IParameter> getParameter(final String name) {
        return cache.compute(createCacheKey(name), (key, cachedItem) -> {
            if (Objects.nonNull(cachedItem)) {
                if (cachedItem.isAlive()) {
                    return cachedItem;
                }
                cachedItem.parameter.ifPresent(parameter -> {
                    events.publish(CacheEventType.EVICTED, this, parameter);
                });
            }
            final Optional<IParameter> result = parameters.getParameter(name);
            result.ifPresent(parameter -> events.publish(CacheEventType.CREATED, this, parameter));
            return new CachedItem(result, updateStrategy.getUpdatePolicy());
        }).getParameter();
    }

    @Override
    public Stream<IParameter> getParameters() {
        return cache.values().stream()
                .filter(CachedItem::isAlive)
                .map(CachedItem::getParameter)
                .filter(Optional::isPresent)
                .map(Optional::get);
    }

    @Override
    public void execute(final CacheCommandType command) {
        switch (command) {
        case FORCE_UPDATE:
            getParameters().collect(Collectors.toList()).forEach(parameter -> {
                cache.computeIfPresent(createCacheKey(parameter.getName()), (n, cachedItem) -> {
                    final Optional<IParameter> updated = parameters.getParameter(parameter.getName());
                    if (!updated.flatMap(u -> cachedItem.parameter.filter(e -> e.getVersion() < u.getVersion())).isPresent()) {
                        updated.ifPresent(u -> events.publish(CacheEventType.UPDATED, this, u));
                        cachedItem.parameter.ifPresent(e -> events.publish(CacheEventType.EVICTED, this, e));
                        return new CachedItem(updated, updateStrategy.getUpdatePolicy());
                    }
                    return cachedItem;
                });
            });
            break;
        case FORCE_EVICT:
            getParameters().collect(Collectors.toList()).forEach(parameter -> {
                final String cacheKey = createCacheKey(parameter.getName());
                if (cache.containsKey(cacheKey)) {
                    cache.remove(cacheKey).parameter.ifPresent(param -> {
                        events.publish(CacheEventType.EVICTED, this, param);
                    });
                }
            });
            break;
        default:
            // do nothing
        }
    }

    @Override
    public String[] getGroupName() {
        return parameters.getGroupName();
    }
}
