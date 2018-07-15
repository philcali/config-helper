package me.philcali.config.system;

import java.util.Arrays;
import java.util.stream.Collectors;

import me.philcali.config.api.IConfigProvider;
import me.philcali.config.api.IParameters;
import me.philcali.config.api.exception.ConfigProvisionException;

public class SystemPropertyConfigProvider implements IConfigProvider {

    @Override
    public IParameters get(final String ... groupName) throws ConfigProvisionException {
        return new SystemPropertyParameters(Arrays.stream(groupName)
                .map(name -> name + ".")
                .collect(Collectors.joining()));
    }
}
