package me.philcali.config.cache.update;

import me.philcali.config.cache.ICacheUpdateStrategy;

public class NeverUpdateStrategyBuilder
        extends CacheConfigUpdateStrategyBuilder<NeverUpdateStrategyBuilder> {
    @Override
    public ICacheUpdateStrategy build() {
        return new DefaultUpdateStrategy(this);
    }
}
