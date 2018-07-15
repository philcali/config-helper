package me.philcali.config.ssm;

import java.util.Arrays;

import com.amazonaws.services.simplesystemsmanagement.model.Parameter;
import com.amazonaws.services.simplesystemsmanagement.model.ParameterType;

import me.philcali.config.api.IParameter;

public class SystemManagerParameter implements IParameter {
    private final Parameter parameter;
    private final String name;

    public SystemManagerParameter(final Parameter parameter) {
        this.parameter = parameter;
        this.name = parameter.getName().substring(parameter.getName().lastIndexOf('/'));
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public Object getValue() {
        switch (ParameterType.valueOf(parameter.getType())) {
        case StringList:
            return Arrays.asList(parameter.getValue().split("\\s*,\\s*"));
        default:
            return parameter.getValue();
        }
    }

    @Override
    public long getVersion() {
        return parameter.getVersion();
    }
}
