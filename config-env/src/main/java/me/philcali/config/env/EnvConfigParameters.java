package me.philcali.config.env;

import java.util.Optional;
import java.util.stream.Stream;

import me.philcali.config.api.IParameter;
import me.philcali.config.api.IParameters;

public class EnvConfigParameters implements IParameters {
    private final String groupName;

    public EnvConfigParameters(final String groupName) {
        this.groupName = groupName;
    }

    private IParameter convertParameter(final String parameterName, final String value) {
        return new EnvConfigParameter(parameterName, value);
    }

    @Override
    public Optional<IParameter> getParameter(final String name) {
        final String parameterName = (groupName + name).toUpperCase();
        return Optional.ofNullable(System.getenv(parameterName))
                .map(value -> convertParameter(parameterName, value));
    }

    @Override
    public Stream<IParameter> getParameters() {
        return System.getenv().keySet().stream()
                .map(this::getParameter)
                .filter(Optional::isPresent)
                .map(Optional::get);
    }

}
