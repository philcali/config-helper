package me.philcali.config.proxy.resolver;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;

public class TypeResolverChain implements ITypeResolver {
    private final List<ITypeResolver> resolvers;

    public TypeResolverChain(final List<ITypeResolver> resolvers,
            final List<Function<ITypeResolver, ITypeResolver>> recusiveResolvers) {
        this.resolvers = new ArrayList<>(resolvers);
        recusiveResolvers.stream().map(thunk -> thunk.apply(this)).forEach(this.resolvers::add);
    }

    @Override
    public Object resolve(final Object value, final Class<?> returnClass, final Type returnType) {
        return resolvers.stream().filter(resolver -> resolver.isApplicable(returnClass)).findFirst()
                .map(resolver -> resolver.resolve(value, returnClass, returnType)).orElse(null);
    }

    @Override
    public boolean isApplicable(final Class<?> returnClass) {
        return resolvers.stream().anyMatch(resolver -> resolver.isApplicable(returnClass));
    }
}
