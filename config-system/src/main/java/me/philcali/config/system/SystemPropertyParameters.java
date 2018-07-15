package me.philcali.config.system;

import java.util.Arrays;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import me.philcali.config.api.IParameter;
import me.philcali.config.api.IParameters;

public class SystemPropertyParameters implements IParameters {
    private final String[] groupName;

    public SystemPropertyParameters(final String[] groupName) {
        this.groupName = groupName;
    }

    private IParameter convertParameter(final String key, final String value) {
        return new SystemPropertyParameter(key, value);
    }

    private String generateSystemName(final String parameterName) {
        return Arrays.stream(groupName)
                .map(name -> name + ".")
                .collect(Collectors.joining()) + parameterName;
    }

    @Override
    public String[] getGroupName() {
        return groupName;
    }

    @Override
    public Optional<IParameter> getParameter(final String name) {
        return Optional.ofNullable(System.getProperty(generateSystemName(name)))
                .map(value -> convertParameter(name, value));
    }

    @Override
    public Stream<IParameter> getParameters() {
        return System.getProperties().keySet().stream()
                .map(String.class::cast)
                .map(this::getParameter)
                .filter(Optional::isPresent)
                .map(Optional::get);
    }

}
