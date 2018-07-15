package me.philcali.config.cache.update;

import me.philcali.config.cache.command.CacheCommandType;

public enum CacheUpdatePolicy {
    SOURCE_UPDATE(CacheCommandType.FORCE_UPDATE),
    TIME_BASED(CacheCommandType.FORCE_EVICT);

    private CacheCommandType command;

    private CacheUpdatePolicy(final CacheCommandType command) {
        this.command = command;
    }

    public CacheCommandType getCommand() {
        return command;
    }
}
