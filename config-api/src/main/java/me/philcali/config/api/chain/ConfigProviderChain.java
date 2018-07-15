package me.philcali.config.api.chain;

import java.util.List;

import me.philcali.config.api.IConfigProvider;
import me.philcali.config.api.IParameters;
import me.philcali.config.api.exception.ConfigProvisionException;

public class ConfigProviderChain implements IConfigProvider {
    private final List<IConfigProvider> providers;

    public ConfigProviderChain(final List<IConfigProvider> providers) {
        this.providers = providers;
    }

    @Override
    public IParameters get(final String ... groupName) throws ConfigProvisionException {
        return new ConfigProviderChainParameters(groupName, providers);
    }
}
