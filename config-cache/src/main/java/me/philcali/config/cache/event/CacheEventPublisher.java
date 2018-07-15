package me.philcali.config.cache.event;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

import me.philcali.config.api.IParameter;
import me.philcali.config.api.IParameters;

public class CacheEventPublisher implements ICacheEventPublisher {
    private final Map<CacheEventType, List<ICacheSubscriber>> subscribers;

    public CacheEventPublisher(final Map<CacheEventType, List<ICacheSubscriber>> subscribers) {
        this.subscribers = subscribers;
    }

    public CacheEventPublisher() {
        this(new ConcurrentHashMap<>());
    }

    public CacheEventPublisher subscribe(final CacheEventType type, final ICacheSubscriber subscriber) {
        final List<ICacheSubscriber> subs = subscribers.compute(type, (t, list) -> Optional
                .ofNullable(list)
                .orElseGet(ArrayList::new));
        subs.add(subscriber);
        return this;
    }

    @Override
    public void publish(final CacheEventType type, final IParameters group, final IParameter parameter) {
        subscribers.getOrDefault(type, Collections.emptyList()).forEach(subscriber -> {
            // TODO: error strategy here
            subscriber.accept(type, group, parameter);
        });
    }

}
