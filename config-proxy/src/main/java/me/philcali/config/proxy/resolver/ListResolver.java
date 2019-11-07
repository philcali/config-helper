package me.philcali.config.proxy.resolver;

import java.lang.reflect.Array;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class ListResolver implements ITypeResolver {
    private final ITypeResolver recursiveResolver;

    public ListResolver(final ITypeResolver recursiveResolver) {
        this.recursiveResolver = recursiveResolver;
    }

    @SuppressWarnings("unchecked")
    @Override
    public Object resolve(final Object value, final Class<?> returnClass, final Type returnType) {
        List<Object> things = null;
        if (value instanceof String) {
            things = Arrays.asList(((String) value).split("\\s*,\\s*"));
        } else if (value instanceof Array) {
            things = Arrays.asList(value);
        } else {
            things = (List<Object>) value;
        }
        final Type childType = ((ParameterizedType) returnType).getActualTypeArguments()[0];
        return things.stream().map(val -> recursiveResolver.resolve(val, (Class<?>) childType, childType))
                .collect(Collectors.toList());
    }

    @Override
    public boolean isApplicable(final Class<?> returnType) {
        return List.class.isAssignableFrom(returnType);
    }

}
