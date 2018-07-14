package me.philcali.config.proxy.resolver;

import java.util.Arrays;

public class DefaultTypeResolverChain extends TypeResolverChain {
    public DefaultTypeResolverChain() {
        super(Arrays.asList(
                new StringResolver(),
                new BooleanResolver(),
                new IntegerResolver(),
                new DoubleResolver(),
                new LongResolver()),
                Arrays.asList(ListResolver::new));
    }
}
