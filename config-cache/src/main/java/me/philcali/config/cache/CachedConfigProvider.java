package me.philcali.config.cache;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import me.philcali.config.api.IConfigProvider;
import me.philcali.config.api.IParameters;
import me.philcali.config.api.exception.ConfigProvisionException;
import me.philcali.config.cache.event.ICacheEventPublisher;

public class CachedConfigProvider implements IConfigProvider {
    private final IConfigProvider provider;
    private final CacheInitializationPolicy initializationPolicy;
    private final ICacheEventPublisher events;
    private final Map<String, IParameters> cache;

    public CachedConfigProvider(
            final IConfigProvider provider,
            final ICacheEventPublisher events,
            final CacheInitializationPolicy initializationPolicy) {
        this.provider = provider;
        this.events = events;
        this.initializationPolicy = initializationPolicy;
        this.cache = new ConcurrentHashMap<>();
    }

    @Override
    public IParameters get(final String ... nameParts) throws ConfigProvisionException {
        return cache.computeIfAbsent(cacheKey(nameParts), key -> {
            return new CachedConfigParameters(provider.get(nameParts), events).initialize(initializationPolicy);
        });
    }

    protected String cacheKey(final String ... nameParts) {
        return String.join(".", nameParts);
    }
}
