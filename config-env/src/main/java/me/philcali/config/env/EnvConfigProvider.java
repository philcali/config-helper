package me.philcali.config.env;

import java.util.Arrays;
import java.util.stream.Collectors;

import me.philcali.config.api.IConfigProvider;
import me.philcali.config.api.IParameters;
import me.philcali.config.api.exception.ConfigProvisionException;

public class EnvConfigProvider implements IConfigProvider {

    @Override
    public IParameters get(final String ... groupName) throws ConfigProvisionException {
        return new EnvConfigParameters(Arrays.stream(groupName)
                .map(name -> name + "_")
                .collect(Collectors.joining("")));
    }
}
