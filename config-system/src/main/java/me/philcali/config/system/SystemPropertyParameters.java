package me.philcali.config.system;

import java.util.Optional;
import java.util.stream.Stream;

import me.philcali.config.api.IParameter;
import me.philcali.config.api.IParameters;

public class SystemPropertyParameters implements IParameters {
    private final String groupName;

    public SystemPropertyParameters(final String groupName) {
        this.groupName = groupName;
    }

    private IParameter convertParameter(final String key, final String value) {
        return new SystemPropertyParameter(groupName, key, value);
    }

    @Override
    public Optional<IParameter> getParameter(final String name) {
        return Optional.ofNullable(System.getProperty(groupName + name))
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
