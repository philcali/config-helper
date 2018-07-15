package me.philcali.config.cache.update;

import java.io.Closeable;
import java.io.IOException;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import me.philcali.config.cache.ICacheUpdateStrategy;
import me.philcali.config.cache.command.CacheCommandType;
import me.philcali.config.cache.command.ICacheCommandExecution;

public class PollingCacheUpdateStrategy implements ICacheUpdateStrategy, Closeable {
    private final CacheUpdatePolicy updatePolicy;
    private final ScheduledExecutorService executor;
    private final TimeUnit timeUnit;
    private final long interval;
    private final long initialDelay;
    private final ICacheCommandExecution commandExecution;

    protected PollingCacheUpdateStrategy(final PollingCacheUpdateStrategyBuilder builder) {
        this.updatePolicy = builder.updatePolicy;
        this.executor = builder.executor;
        this.timeUnit = builder.timeUnit;
        this.interval = builder.interval;
        this.initialDelay = builder.initialDelay;
        this.commandExecution = builder.commandExecution;
    }

    private Runnable safeRunnable() {
        return () -> {
            try {
                commandExecution.execute(updatePolicy.getCommand());
            } catch (Throwable t) {
                // TODO: log this
            }
        };
    }

    @Override
    public void send(final CacheCommandType command) {
        commandExecution.execute(command);
    }

    @Override
    public void run() {
        executor.scheduleWithFixedDelay(safeRunnable(), initialDelay, interval, timeUnit);
    }

    @Override
    public void close() throws IOException {
        executor.shutdown();
    }
}
