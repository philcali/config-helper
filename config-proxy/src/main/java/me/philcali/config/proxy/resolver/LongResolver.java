package me.philcali.config.proxy.resolver;

public class LongResolver implements ITypeLookup {

    @Override
    public Object resolve(final Object value, final Class<?> returnType) {
        return Long.parseLong(value.toString());
    }

    @Override
    public boolean isApplicable(final Class<?> returnType) {
        return long.class.isAssignableFrom(returnType)
                || Long.class.isAssignableFrom(returnType);
    }

}
