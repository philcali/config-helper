package me.philcali.config.cache.update;

import java.io.Closeable;
import java.io.IOException;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import me.philcali.config.cache.command.CacheCommandType;

public class PollingCacheUpdateStrategy extends DefaultUpdateStrategy implements Closeable {
    private static final Logger LOGGER = LoggerFactory.getLogger(PollingCacheUpdateStrategy.class);
    private final ScheduledExecutorService executor;
    private final TimeUnit timeUnit;
    private final long interval;
    private final long initialDelay;

    protected PollingCacheUpdateStrategy(final PollingCacheUpdateStrategyBuilder builder) {
        super(builder);
        this.executor = builder.executor;
        this.timeUnit = builder.timeUnit;
        this.interval = builder.interval;
        this.initialDelay = builder.initialDelay;
        executor.scheduleWithFixedDelay(safeRunnable(), initialDelay, interval, timeUnit);
    }

    private Runnable safeRunnable() {
        return () -> {
            try {
                commandExecution.execute(CacheCommandType.FORCE_UPDATE);
            } catch (Throwable t) {
                LOGGER.error("Failed to poll execute update:", t);
            }
        };
    }

    @Override
    public void close() throws IOException {
        executor.shutdown();
    }
}
