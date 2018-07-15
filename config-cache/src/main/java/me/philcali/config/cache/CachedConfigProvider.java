package me.philcali.config.cache;

import java.util.Map;
import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;

import me.philcali.config.api.IConfigProvider;
import me.philcali.config.api.IParameters;
import me.philcali.config.api.exception.ConfigProvisionException;
import me.philcali.config.cache.event.CacheEventPublisher;
import me.philcali.config.cache.event.ICacheEventPublisher;
import me.philcali.config.cache.update.CacheConfigUpdateStrategyBuilder;
import me.philcali.config.cache.update.CacheEvictionPolicy;

public class CachedConfigProvider implements IConfigProvider {
    public static final class Builder {
        private IConfigProvider configProvider;
        private ICacheUpdateStrategy updateStrategy;
        private ICacheEventPublisher eventPublisher;
        private CacheInitializationPolicy initializationPolicy;

        public Builder withConfigProvider(final IConfigProvider configProvider) {
            this.configProvider = configProvider;
            return this;
        }

        public Builder withEventPublisher(final ICacheEventPublisher eventPublisher) {
            this.eventPublisher = eventPublisher;
            return this;
        }

        public Builder withUpdateStrategy(final ICacheUpdateStrategy updateStrategy) {
            this.updateStrategy = updateStrategy;
            return this;
        }

        public Builder withInitializationPolicy(final CacheInitializationPolicy initializationPolicy) {
            this.initializationPolicy = initializationPolicy;
            return this;
        }

        public IConfigProvider build() {
            Objects.requireNonNull(configProvider, "Caching provider must wrap a config provider");
            if (Objects.isNull(initializationPolicy)) {
                initializationPolicy = CacheInitializationPolicy.LAZY;
            }
            if (Objects.isNull(eventPublisher)) {
                eventPublisher = new CacheEventPublisher();
            }
            if (Objects.isNull(updateStrategy)) {
                updateStrategy = CacheConfigUpdateStrategyBuilder.neverUpdating()
                        .withEvictionPolicy(CacheEvictionPolicy.TIME_BASED)
                        .build();
            }
            return new CachedConfigProvider(this);
        }
    }

    public static Builder builder() {
        return new Builder();
    }

    private final IConfigProvider provider;
    private final CacheInitializationPolicy initializationPolicy;
    private final ICacheEventPublisher events;
    private final ICacheUpdateStrategy updateStrategy;
    private final Map<String, IParameters> cache;

    public CachedConfigProvider(final Builder builder) {
        this.provider = builder.configProvider;
        this.events = builder.eventPublisher;
        this.initializationPolicy = builder.initializationPolicy;
        this.updateStrategy = builder.updateStrategy;
        this.cache = new ConcurrentHashMap<>();
    }

    @Override
    public IParameters get(final String ... nameParts) throws ConfigProvisionException {
        return cache.computeIfAbsent(cacheKey(nameParts), key -> {
            return new CachedConfigParameters(provider.get(nameParts), events, updateStrategy)
                    .initialize(initializationPolicy);
        });
    }

    protected String cacheKey(final String ... nameParts) {
        return String.join(".", nameParts);
    }
}
