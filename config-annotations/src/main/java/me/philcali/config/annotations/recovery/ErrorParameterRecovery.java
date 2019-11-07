package me.philcali.config.annotations.recovery;

import me.philcali.config.annotations.exception.ConfigParameterNotFoundException;

public class ErrorParameterRecovery implements IParameterRecovery {
    @Override
    public Object recover(final String groupName, final String ... parameterName) {
        throw new ConfigParameterNotFoundException(groupName, parameterName);
    }
}
