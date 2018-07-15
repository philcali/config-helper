package me.philcali.config.cache;

import java.util.concurrent.TimeUnit;

import me.philcali.config.cache.update.CacheEvictionPolicy;

public class CacheUpdatePolicy {
    private final CacheEvictionPolicy evictionPolicy;
    private final TimeUnit timeUnit;
    private final long amount;

    public CacheUpdatePolicy(
            final CacheEvictionPolicy evictionPolicy,
            final TimeUnit timeUnit,
            final long amount) {
        this.evictionPolicy = evictionPolicy;
        this.timeUnit = timeUnit;
        this.amount = amount;
    }

    public long getAmount() {
        return amount;
    }

    public TimeUnit getTimeUnit() {
        return timeUnit;
    }

    public CacheEvictionPolicy getEvictionPolicy() {
        return evictionPolicy;
    }
}
