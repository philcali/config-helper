package me.philcali.config.proxy.resolver;

public interface ITypeResolver {
    Object resolve(Object value, Class<?> returnType);
}
