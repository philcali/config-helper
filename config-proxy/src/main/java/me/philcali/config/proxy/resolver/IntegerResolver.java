package me.philcali.config.proxy.resolver;

import java.lang.reflect.Type;

public class IntegerResolver implements ITypeLookup {

    @Override
    public boolean isApplicable(final Class<?> returnType) {
        return int.class.isAssignableFrom(returnType)
                || Integer.class.isAssignableFrom(returnType);
    }

    @Override
    public Object resolve(final Object value, final Class<?> returnClass, final Type returnType) {
        return Integer.parseInt(value.toString());
    }
}
