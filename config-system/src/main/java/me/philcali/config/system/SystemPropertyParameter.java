package me.philcali.config.system;

import me.philcali.config.api.IParameter;

public class SystemPropertyParameter implements IParameter {
    private final String groupName;
    private final String name;
    private final String value;

    public SystemPropertyParameter(final String groupName, final String name, final String value) {
        this.groupName = groupName;
        this.name = name;
        this.value = value;
    }

    @Override
    public String getName() {
        return groupName + "." + name;
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
