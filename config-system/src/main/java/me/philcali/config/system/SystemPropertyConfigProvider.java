package me.philcali.config.system;

import me.philcali.config.api.IConfigProvider;
import me.philcali.config.api.IParameters;
import me.philcali.config.api.exception.ConfigProvisionException;

public class SystemPropertyConfigProvider implements IConfigProvider {

    @Override
    public IParameters get(final String groupName) throws ConfigProvisionException {
        return new SystemPropertyParameters(groupName);
    }
}
