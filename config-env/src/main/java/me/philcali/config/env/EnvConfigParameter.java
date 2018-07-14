package me.philcali.config.env;

import me.philcali.config.api.IParameter;

public class EnvConfigParameter implements IParameter {
    private final String parameterName;
    private final String value;

    public EnvConfigParameter(final String parameterName, final String value) {
        this.parameterName = parameterName;
        this.value = value;
    }

    @Override
    public String getName() {
        return parameterName;
    }

    @Override
    public Object getValue() {
        return value;
    }

    @Override
    public long getVersion() {
        return 0;
    }
}
