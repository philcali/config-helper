package me.philcali.config.api.chain;

import java.util.List;
import java.util.Optional;
import java.util.function.Function;
import java.util.stream.Stream;

import me.philcali.config.api.IConfigProvider;
import me.philcali.config.api.IParameter;
import me.philcali.config.api.IParameters;

public class ConfigProviderChainParameters implements IParameters {
    private final String[] groupName;
    private final List<IConfigProvider> providers;

    public ConfigProviderChainParameters(
            final String[] groupName,
            final List<IConfigProvider> providers) {
        this.groupName = groupName;
        this.providers = providers;
    }

    @Override
    public Optional<IParameter> getParameter(final String name) {
        return providers.stream()
                .map(provider -> provider.get(groupName).getParameter(name))
                .filter(Optional::isPresent)
                .findFirst()
                .flatMap(Function.identity());
    }

    @Override
    public Stream<IParameter> getParameters() {
        return providers.stream().flatMap(provider -> provider.get(groupName).getParameters());
    }
}
