package me.philcali.config.api.chain;

import java.util.List;
import java.util.Optional;
import java.util.function.Function;
import java.util.stream.Stream;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import me.philcali.config.api.IConfigProvider;
import me.philcali.config.api.IParameter;
import me.philcali.config.api.IParameters;

public class ConfigProviderChainParameters implements IParameters {
    private static final Logger LOGGER = LoggerFactory.getLogger(ConfigProviderChainParameters.class);
    private final String[] groupName;
    private final List<IConfigProvider> providers;

    public ConfigProviderChainParameters(
            final String[] groupName,
            final List<IConfigProvider> providers) {
        this.groupName = groupName;
        this.providers = providers;
    }

    @Override
    public String[] getGroupName() {
        return groupName;
    }

    @Override
    public Optional<IParameter> getParameter(final String name) {
        return providers.stream()
                .peek(provider -> LOGGER.info("Using provider {} for {} {}", provider.getClass(), groupName, name))
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
