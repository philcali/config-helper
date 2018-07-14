package me.philcali.config.proxy.resolver;

public class DoubleResolver implements ITypeLookup {
    @Override
    public boolean isApplicable(final Class<?> returnType) {
        return double.class.isAssignableFrom(returnType)
                || Double.class.isAssignableFrom(returnType);
    }

    @Override
    public Object resolve(final Object value, final Class<?> returnType) {
        return Double.parseDouble(value.toString());
    }
}
