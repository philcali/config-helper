package me.philcali.config.api.chain;

import java.util.ServiceLoader;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import me.philcali.config.api.IConfigProvider;

public class DefaultConfigProviderChain extends ConfigProviderChain {
    public DefaultConfigProviderChain() {
        // This is chosen over the system loader.
        this(DefaultConfigProviderChain.class.getClassLoader());
    }

    public DefaultConfigProviderChain(final ClassLoader loader) {
        super(StreamSupport.stream(ServiceLoader.load(IConfigProvider.class, loader).spliterator(), false)
                .collect(Collectors.toList()));
    }
}
