package me.philcali.config.proxy.resolver;

import java.lang.reflect.Array;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class ListResolver implements ITypeLookup {
    private final ITypeResolver recursiveResolver;

    public ListResolver(final ITypeResolver recursiveResolver) {
        this.recursiveResolver = recursiveResolver;
    }

    @SuppressWarnings("unchecked")
    @Override
    public Object resolve(final Object value, final Class<?> returnType) {
        List<Object> things = null;
        if (value instanceof String) {
            things = Arrays.asList(((String) value).split("\\s*,\\s*"));
        } else if (value instanceof Array) {
            things = Arrays.asList(value);
        } else {
            things = (List<Object>) value;
        }
        return things.stream()
                .map(val -> recursiveResolver.resolve(val, returnType))
                .collect(Collectors.toList());
    }

    @Override
    public boolean isApplicable(final Class<?> returnType) {
        return List.class.isAssignableFrom(returnType);
    }

}
