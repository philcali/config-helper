package me.philcali.config.proxy.resolver;

import java.lang.reflect.Type;

public class BooleanResolver implements ITypeResolver {

    @Override
    public Object resolve(final Object value, final Class<?> returnClass, final Type returnType) {
        if (value.equals(1)) {
            return true;
        } else if (value.equals(0)) {
            return false;
        } else {
            return Boolean.parseBoolean(value.toString());
        }
    }

    @Override
    public boolean isApplicable(final Class<?> returnType) {
        return boolean.class.isAssignableFrom(returnType) || Boolean.class.isAssignableFrom(returnType);
    }

}
