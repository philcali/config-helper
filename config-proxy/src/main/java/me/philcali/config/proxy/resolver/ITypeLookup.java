package me.philcali.config.proxy.resolver;

public interface ITypeLookup extends ITypeResolver {
    boolean isApplicable(Class<?> returnType);
}
