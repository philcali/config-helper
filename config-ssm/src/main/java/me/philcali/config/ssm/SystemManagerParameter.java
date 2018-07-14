package me.philcali.config.ssm;

import com.amazonaws.services.simplesystemsmanagement.model.Parameter;
import com.amazonaws.services.simplesystemsmanagement.model.ParameterType;

import me.philcali.config.api.IParameter;

public class SystemManagerParameter implements IParameter {
    private final Parameter parameter;

    public SystemManagerParameter(final Parameter parameter) {
        this.parameter = parameter;
    }

    @Override
    public String getName() {
        return parameter.getName();
    }

    @Override
    public Object getValue() {
        switch (ParameterType.valueOf(parameter.getType())) {
        case StringList:
            return parameter.getValue().split("\\s*,\\s*");
        default:
            return parameter.getValue();
        }
    }

    @Override
    public long getVersion() {
        return parameter.getVersion();
    }
}
