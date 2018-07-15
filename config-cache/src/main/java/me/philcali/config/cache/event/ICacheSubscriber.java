package me.philcali.config.cache.event;

import me.philcali.config.api.IParameter;
import me.philcali.config.api.IParameters;

public interface ICacheSubscriber {
    void accept(final CacheEventType type, final IParameters group, final IParameter parameter);
}
