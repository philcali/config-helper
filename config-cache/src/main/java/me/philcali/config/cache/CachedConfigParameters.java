package me.philcali.config.cache;

import java.util.Map;
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

public class CachedConfigParameters implements IParameters, ICacheCommandExecution {
    private final IParameters parameters;
    private final ICacheEventPublisher events;
    private final Map<String, Optional<IParameter>> cache;

    public CachedConfigParameters(final IParameters parameters, final ICacheEventPublisher events) {
        this.parameters = parameters;
        this.events = events;
        this.cache = new ConcurrentHashMap<>();
    }

    public CachedConfigParameters initialize(final CacheInitializationPolicy initializationPolicy) {
        switch (initializationPolicy) {
        case FILL:
            parameters.getParameters().forEach(parameter -> {
                cache.computeIfAbsent(createCacheKey(parameter.getName()), key -> {
                    final Optional<IParameter> param = Optional.ofNullable(parameter);
                    events.publish(CacheEventType.CREATED, this, parameter);
                    return param;
                });
            });
            break;
        default:
            // do nothing
        }
        return this;
    }

    private String createCacheKey(final String name) {
        return "CACHE." + String.join(".", parameters.getGroupName()) + "." + name;
    }

    @Override
    public Optional<IParameter> getParameter(final String name) {
        return cache.computeIfAbsent(createCacheKey(name), n -> {
            final Optional<IParameter> result = parameters.getParameter(name);
            result.ifPresent(parameter -> events.publish(CacheEventType.CREATED, this, parameter));
            return result;
        });
    }

    @Override
    public Stream<IParameter> getParameters() {
        return cache.values().stream()
                .filter(Optional::isPresent)
                .map(Optional::get);
    }

    @Override
    public void execute(final CacheCommandType command) {
        switch (command) {
        case FORCE_UPDATE:
            getParameters().collect(Collectors.toList()).forEach(parameter -> {
                cache.computeIfPresent(createCacheKey(parameter.getName()), (n, existing) -> {
                    final Optional<IParameter> updated = parameters.getParameter(parameter.getName());
                    if (!updated.flatMap(u -> existing.filter(e -> e.getVersion() < u.getVersion())).isPresent()) {
                        updated.ifPresent(u -> events.publish(CacheEventType.UPDATED, this, u));
                        existing.ifPresent(e -> events.publish(CacheEventType.EVICTED, this, e));
                        return updated;
                    }
                    return existing;
                });
            });
            break;
        case FORCE_EVICT:
            getParameters().collect(Collectors.toList()).forEach(parameter -> {
                final String cacheKey = createCacheKey(parameter.getName());
                if (cache.containsKey(cacheKey)) {
                    cache.remove(cacheKey).ifPresent(param -> {
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
