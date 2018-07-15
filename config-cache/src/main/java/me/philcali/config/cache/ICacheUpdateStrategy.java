package me.philcali.config.cache;

import me.philcali.config.cache.command.CacheCommandType;

public interface ICacheUpdateStrategy {
    CacheUpdatePolicy getUpdatePolicy();

    void send(CacheCommandType command);
}
