package me.philcali.config.proxy;

import java.lang.reflect.Proxy;
import java.util.Optional;

import me.philcali.config.api.IConfigFactory;

public class ConfigProxyFactory implements IConfigFactory {
    private final ConfigProxyFactoryOptions options;

    public ConfigProxyFactory(final ConfigProxyFactoryOptions options) {
        this.options = options;
    }

    public ConfigProxyFactory() {
        this(ConfigProxyFactoryOptions.builder().build());
    }

    @SuppressWarnings("unchecked")
    @Override
    public <T> T create(final Class<T> configurationClass, final Optional<String> parameterGroup,
            final String... parameters) {
        return (T) Proxy.newProxyInstance(configurationClass.getClassLoader(), new Class[] { configurationClass },
                new ConfigInvocationHandler(this, options, parameterGroup, parameters));
    }
}
