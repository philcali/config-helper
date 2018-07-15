package me.philcali.config.proxy.name;

import java.util.ArrayList;
import java.util.List;

public class DefaultParameterGroupPrefix implements IParameterGroupReplacement {
    private final List<String> prefixParts;

    public DefaultParameterGroupPrefix(final List<String> prefixParts) {
        this.prefixParts = prefixParts;
    }

    public DefaultParameterGroupPrefix() {
        this(new ArrayList<>());
    }

    public DefaultParameterGroupPrefix addPart(final String part) {
        this.prefixParts.add(part);
        return this;
    }

    @Override
    public String[] replace(final String groupName) {
        final String[] parts = new String[prefixParts.size() + 1];
        int index;
        for (index = 0; index < prefixParts.size(); index++) {
            parts[index] = prefixParts.get(index);
        }
        parts[index] = groupName;
        return parts;
    }
}
