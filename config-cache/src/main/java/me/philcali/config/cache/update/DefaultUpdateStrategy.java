package me.philcali.config.cache.update;

import me.philcali.config.cache.CacheUpdatePolicy;
import me.philcali.config.cache.ICacheUpdateStrategy;
import me.philcali.config.cache.command.CacheCommandType;
import me.philcali.config.cache.command.ICacheCommandExecution;

public class DefaultUpdateStrategy implements ICacheUpdateStrategy {
    protected final ICacheCommandExecution commandExecution;
    private final CacheUpdatePolicy updatePolicy;

    protected DefaultUpdateStrategy(final CacheConfigUpdateStrategyBuilder<?> builder) {
        this.commandExecution = builder.commandExecution;
        this.updatePolicy = new CacheUpdatePolicy(
                builder.evictionPolicy,
                builder.evictionTimeUnit,
                builder.evictionAmount);
    }

    @Override
    public void send(final CacheCommandType command) {
        commandExecution.execute(command);
    }

    @Override
    public CacheUpdatePolicy getUpdatePolicy() {
        return updatePolicy;
    }
}
