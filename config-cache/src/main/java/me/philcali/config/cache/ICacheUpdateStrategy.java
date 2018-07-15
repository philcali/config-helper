package me.philcali.config.cache;

import me.philcali.config.cache.command.CacheCommandType;

public interface ICacheUpdateStrategy extends Runnable {
    void send(CacheCommandType command);
}
