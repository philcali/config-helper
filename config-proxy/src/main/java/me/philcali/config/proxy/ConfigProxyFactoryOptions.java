package me.philcali.config.proxy;

import java.util.Objects;

import me.philcali.config.api.IConfigProvider;
import me.philcali.config.api.chain.DefaultConfigProviderChain;
import me.philcali.config.proxy.name.DefaultParameterGroupPrefix;
import me.philcali.config.proxy.name.IParameterGroupReplacement;
import me.philcali.config.proxy.resolver.DefaultTypeResolverChain;
import me.philcali.config.proxy.resolver.ITypeResolver;

public class ConfigProxyFactoryOptions {
    public static class Builder {
        private ITypeResolver typeResolver;
        private IParameterGroupReplacement groupPrefix;
        private IConfigProvider configProvider;

        public Builder withConfigProvider(final IConfigProvider configProvider) {
            this.configProvider = configProvider;
            return this;
        }

        public Builder withTypeResolver(final ITypeResolver typeResolver) {
            this.typeResolver = typeResolver;
            return this;
        }

        public Builder withGroupPrefix(final IParameterGroupReplacement groupPrefix) {
            this.groupPrefix = groupPrefix;
            return this;
        }

        public ConfigProxyFactoryOptions build() {
            if (Objects.isNull(configProvider)) {
                configProvider = new DefaultConfigProviderChain();
            }
            if (Objects.isNull(groupPrefix)) {
                groupPrefix = new DefaultParameterGroupPrefix();
            }
            if (Objects.isNull(typeResolver)) {
                typeResolver = new DefaultTypeResolverChain();
            }
            return new ConfigProxyFactoryOptions(this);
        }
    }

    public static Builder builder() {
        return new Builder();
    }

    private final IConfigProvider configProvider;
    private final ITypeResolver typeResolver;
    private final IParameterGroupReplacement groupPrefix;

    private ConfigProxyFactoryOptions(final Builder builder) {
        this.typeResolver = builder.typeResolver;
        this.groupPrefix = builder.groupPrefix;
        this.configProvider = builder.configProvider;
    }

    public IParameterGroupReplacement getGroupPrefix() {
        return groupPrefix;
    }

    public ITypeResolver getTypeResolver() {
        return typeResolver;
    }

    public IConfigProvider getConfigProvider() {
        return configProvider;
    }
}
