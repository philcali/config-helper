package me.philcali.config.cache.event;

import me.philcali.config.api.IParameter;
import me.philcali.config.api.IParameters;

public interface ICacheEventPublisher {
    void publish(CacheEventType type, IParameters group, IParameter parameter);
}
