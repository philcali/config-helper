package me.philcali.config.annotations.recovery;

public class NullableParameterRecovery implements IParameterRecovery {
    @Override
    public Object recover(final String groupName, final String ... parameterName) {
        // If the parameter does not exist... then return null;
        return null;
    }
}
