package me.philcali.config.proxy.resolver;

import java.lang.reflect.Type;

public class DoubleResolver implements ITypeResolver {
    @Override
    public boolean isApplicable(final Class<?> returnType) {
        return double.class.isAssignableFrom(returnType) || Double.class.isAssignableFrom(returnType);
    }

    @Override
    public Object resolve(final Object value, final Class<?> returnClass, final Type returnType) {
        return Double.parseDouble(value.toString());
    }
}
