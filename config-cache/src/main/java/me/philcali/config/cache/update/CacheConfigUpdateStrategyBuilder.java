package me.philcali.config.cache.update;

import me.philcali.config.cache.ICacheUpdateStrategy;
import me.philcali.config.cache.command.ICacheCommandExecution;

public abstract class CacheConfigUpdateStrategyBuilder<T extends CacheConfigUpdateStrategyBuilder<T>> {
    protected ICacheCommandExecution commandExecution;

    public T withCommandExecution(final ICacheCommandExecution commandExecution) {
        this.commandExecution = commandExecution;
        return (T) this;
    }

    public static PollingCacheUpdateStrategyBuilder sourceUpdating() {
        return new PollingCacheUpdateStrategyBuilder().withUpdatePolicy(CacheUpdatePolicy.SOURCE_UPDATE);
    }

    public static PollingCacheUpdateStrategyBuilder timeEvicting() {
        return new PollingCacheUpdateStrategyBuilder().withUpdatePolicy(CacheUpdatePolicy.TIME_BASED);
    }

    public static NeverUpdateStrategyBuilder neverUpdating() {
        return new NeverUpdateStrategyBuilder();
    }

    public abstract ICacheUpdateStrategy build();
}
