package me.philcali.config.proxy.resolver;

import java.lang.reflect.Type;

public interface ITypeResolver {
    Object resolve(Object value, Class<?> returnClass, Type returnType);

    boolean isApplicable(Class<?> returnClass);
}
