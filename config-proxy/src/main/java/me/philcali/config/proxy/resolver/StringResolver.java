package me.philcali.config.proxy.resolver;

public class StringResolver implements ITypeLookup {

    @Override
    public boolean isApplicable(final Class<?> returnType) {
        return returnType.equals(String.class);
    }

    @Override
    public String resolve(final Object value, final Class<?> returnType) {
        return value.toString();
    }
}
