package me.philcali.config.cache.update;

import java.util.Objects;
import java.util.Optional;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import me.philcali.config.cache.ICacheUpdateStrategy;

public class PollingCacheUpdateStrategyBuilder
        extends CacheConfigUpdateStrategyBuilder<PollingCacheUpdateStrategyBuilder> {

    protected CacheUpdatePolicy updatePolicy;
    protected int corePoolSize = 1;
    protected TimeUnit timeUnit = TimeUnit.MINUTES;
    protected long interval = 1L;
    protected long initialDelay = 0L;
    protected ScheduledExecutorService executor;

    public PollingCacheUpdateStrategyBuilder withCorePoolSize(final int corePoolSize) {
        this.corePoolSize = corePoolSize;
        return this;
    }

    public PollingCacheUpdateStrategyBuilder withTimeUnit(final TimeUnit timeUnit) {
        this.timeUnit = timeUnit;
        return this;
    }

    public PollingCacheUpdateStrategyBuilder withInterval(final long interval) {
        this.interval = interval;
        return this;
    }

    public PollingCacheUpdateStrategyBuilder withInitialDelay(final long initialDelay) {
        this.initialDelay = initialDelay;
        return this;
    }

    public PollingCacheUpdateStrategyBuilder withExecutor(final ScheduledExecutorService executor) {
        this.executor = executor;
        return this;
    }

    public PollingCacheUpdateStrategyBuilder withUpdatePolicy(final CacheUpdatePolicy updatePolicy) {
        this.updatePolicy = updatePolicy;
        return this;
    }

    @Override
    public ICacheUpdateStrategy build() {
        Objects.requireNonNull(updatePolicy, "Missing required update policy");
        executor = Optional.ofNullable(executor).orElseGet(this::getDefaultExecutor);
        return new PollingCacheUpdateStrategy(this);
    }

    protected ScheduledExecutorService getDefaultExecutor() {
        return Executors.newScheduledThreadPool(corePoolSize, runnable -> {
            final Thread thread = new Thread(runnable);
            thread.setDaemon(true);
            return thread;
        });
    }
}
