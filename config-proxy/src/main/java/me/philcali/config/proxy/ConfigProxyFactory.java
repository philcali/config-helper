package me.philcali.config.proxy;

import java.lang.reflect.Proxy;
import java.util.Optional;

import me.philcali.config.api.IConfigFactory;
import me.philcali.config.api.IConfigProvider;
import me.philcali.config.api.chain.DefaultConfigProviderChain;
import me.philcali.config.proxy.resolver.DefaultTypeResolverChain;
import me.philcali.config.proxy.resolver.ITypeResolver;

public class ConfigProxyFactory implements IConfigFactory {
    private final IConfigProvider provider;
    private final ITypeResolver resolver;

    public ConfigProxyFactory(final IConfigProvider provider, final ITypeResolver resolver) {
        this.provider = provider;
        this.resolver = resolver;
    }

    public ConfigProxyFactory(final IConfigProvider provider) {
        this(provider, new DefaultTypeResolverChain());
    }

    public ConfigProxyFactory() {
        this(new DefaultConfigProviderChain());
    }

    @SuppressWarnings("unchecked")
    @Override
    public <T> T create(final Class<T> configurationClass, final Optional<String> parameterGroup) {
        return (T) Proxy.newProxyInstance(
                configurationClass.getClassLoader(),
                new Class[] { configurationClass },
                new ConfigInvocationHandler(provider, resolver, parameterGroup));
    }
}
