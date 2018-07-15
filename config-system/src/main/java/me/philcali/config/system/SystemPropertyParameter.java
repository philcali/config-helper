package me.philcali.config.system;

import me.philcali.config.api.IParameter;

public class SystemPropertyParameter implements IParameter {
    private final String name;
    private final String value;

    public SystemPropertyParameter(final String name, final String value) {
        this.name = name;
        this.value = value;
    }

    @Override
    public String getName() {
        return name;
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
