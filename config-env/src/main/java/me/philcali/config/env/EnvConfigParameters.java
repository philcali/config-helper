package me.philcali.config.env;

import java.util.Arrays;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import me.philcali.config.api.IParameter;
import me.philcali.config.api.IParameters;

public class EnvConfigParameters implements IParameters {
    private final String[] groupName;

    public EnvConfigParameters(final String[] groupName) {
        this.groupName = groupName;
    }

    private IParameter convertParameter(final String parameterName, final String value) {
        return new EnvConfigParameter(parameterName, value);
    }

    private String generateEnvName(final String parameterName) {
        return (Arrays.stream(groupName)
                .map(name -> name + "_")
                .collect(Collectors.joining("")) + parameterName).toUpperCase();
    }

    @Override
    public Optional<IParameter> getParameter(final String name) {
        return Optional.ofNullable(System.getenv(generateEnvName(name)))
                .map(value -> convertParameter(name, value));
    }

    @Override
    public Stream<IParameter> getParameters() {
        return System.getenv().keySet().stream()
                .map(this::getParameter)
                .filter(Optional::isPresent)
                .map(Optional::get);
    }

    @Override
    public String[] getGroupName() {
        return groupName;
    }

}
