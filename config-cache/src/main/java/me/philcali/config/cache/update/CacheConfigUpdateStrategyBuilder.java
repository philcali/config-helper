package me.philcali.config.cache.update;

import java.util.concurrent.TimeUnit;

import me.philcali.config.cache.ICacheUpdateStrategy;
import me.philcali.config.cache.command.ICacheCommandExecution;

public abstract class CacheConfigUpdateStrategyBuilder<T extends CacheConfigUpdateStrategyBuilder<T>> {
    protected ICacheCommandExecution commandExecution;
    protected CacheEvictionPolicy evictionPolicy = CacheEvictionPolicy.NEVER;
    protected TimeUnit evictionTimeUnit = TimeUnit.MINUTES;
    protected long evictionAmount = 5L;

    public T withCommandExecution(final ICacheCommandExecution commandExecution) {
        this.commandExecution = commandExecution;
        return (T) this;
    }

    public T withEvictionPolicy(final CacheEvictionPolicy evictionPolicy) {
        this.evictionPolicy = evictionPolicy;
        return (T) this;
    }

    public T withEvictionTimeUnit(final TimeUnit evictionTimeUnit) {
        this.evictionTimeUnit = evictionTimeUnit;
        return (T) this;
    }

    public T withEvictionAmount(final long evictionAmount) {
        this.evictionAmount = evictionAmount;
        return (T) this;
    }

    public static PollingCacheUpdateStrategyBuilder sourceUpdating() {
        return new PollingCacheUpdateStrategyBuilder();
    }

    public static NeverUpdateStrategyBuilder neverUpdating() {
        return new NeverUpdateStrategyBuilder();
    }

    public abstract ICacheUpdateStrategy build();
}
