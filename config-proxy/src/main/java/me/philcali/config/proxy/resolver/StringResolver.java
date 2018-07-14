package me.philcali.config.proxy.resolver;

import java.lang.reflect.Type;

public class StringResolver implements ITypeLookup {

    @Override
    public boolean isApplicable(final Class<?> returnType) {
        return returnType.equals(String.class);
    }

    @Override
    public String resolve(final Object value, final Class<?> returnClass, final Type returnType) {
        return value.toString();
    }
}
