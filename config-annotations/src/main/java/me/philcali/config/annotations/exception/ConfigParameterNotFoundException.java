package me.philcali.config.annotations.exception;

public class ConfigParameterNotFoundException extends RuntimeException {
    private static final long serialVersionUID = 4335100819824814732L;
    private final String groupName;
    private final String[] parameterName;

    public ConfigParameterNotFoundException(final String groupName, final String ... parameterName) {
        this.groupName = groupName;
        this.parameterName = parameterName;
    }

    public String getGroupName() {
        return groupName;
    }

    public String[] getParameterName() {
        return parameterName;
    }
}
