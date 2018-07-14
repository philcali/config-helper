package me.philcali.config.proxy.resolver;

public class BooleanResolver implements ITypeLookup {

    @Override
    public Object resolve(Object value, Class<?> returnType) {
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
        return boolean.class.isAssignableFrom(returnType)
                || Boolean.class.isAssignableFrom(returnType);
    }

}
