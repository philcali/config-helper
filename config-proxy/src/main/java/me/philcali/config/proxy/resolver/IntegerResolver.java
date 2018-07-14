package me.philcali.config.proxy.resolver;

public class IntegerResolver implements ITypeLookup {

    @Override
    public boolean isApplicable(final Class<?> returnType) {
        return int.class.isAssignableFrom(returnType)
                || Integer.class.isAssignableFrom(returnType);
    }

    @Override
    public Object resolve(final Object value, final Class<?> returnType) {
        return Integer.parseInt(value.toString());
    }
}
