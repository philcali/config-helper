package me.philcali.config.proxy.resolver;

import java.lang.reflect.Type;

public class LongResolver implements ITypeResolver {

    @Override
    public Object resolve(final Object value, final Class<?> returnClass, final Type returnType) {
        return Long.parseLong(value.toString());
    }

    @Override
    public boolean isApplicable(final Class<?> returnType) {
        return long.class.isAssignableFrom(returnType) || Long.class.isAssignableFrom(returnType);
    }

}
