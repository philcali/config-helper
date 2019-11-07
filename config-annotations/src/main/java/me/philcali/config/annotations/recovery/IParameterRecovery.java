package me.philcali.config.annotations.recovery;

public interface IParameterRecovery {
    Object recover(final String groupName, final String ... parameterName);
}
