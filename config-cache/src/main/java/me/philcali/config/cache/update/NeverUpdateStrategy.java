package me.philcali.config.cache.update;

import me.philcali.config.cache.ICacheUpdateStrategy;
import me.philcali.config.cache.command.CacheCommandType;
import me.philcali.config.cache.command.ICacheCommandExecution;

public class NeverUpdateStrategy implements ICacheUpdateStrategy {
    private final ICacheCommandExecution commandExecution;

    protected NeverUpdateStrategy(final CacheConfigUpdateStrategyBuilder<?> builder) {
        this.commandExecution = builder.commandExecution;
    }

    @Override
    public void send(final CacheCommandType command) {
        commandExecution.execute(command);
    }

    @Override
    public void run() {
        // do nothing
    }
}
