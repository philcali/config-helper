package me.philcali.config.env;

import me.philcali.config.api.IConfigProvider;
import me.philcali.config.api.IParameters;
import me.philcali.config.api.exception.ConfigProvisionException;

public class EnvConfigProvider implements IConfigProvider {

    @Override
    public IParameters get(final String ... groupName) throws ConfigProvisionException {
        return new EnvConfigParameters(groupName);
    }
}
