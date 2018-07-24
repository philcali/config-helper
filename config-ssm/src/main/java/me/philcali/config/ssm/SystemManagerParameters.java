package me.philcali.config.ssm;

import java.util.Arrays;
import java.util.Optional;
import java.util.Spliterator;
import java.util.Spliterators;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.amazonaws.SdkBaseException;
import com.amazonaws.services.simplesystemsmanagement.AWSSimpleSystemsManagement;
import com.amazonaws.services.simplesystemsmanagement.model.GetParameterRequest;
import com.amazonaws.services.simplesystemsmanagement.model.Parameter;
import com.amazonaws.services.simplesystemsmanagement.model.ParameterNotFoundException;

import me.philcali.config.api.IParameter;
import me.philcali.config.api.IParameters;

public class SystemManagerParameters implements IParameters {
    private static final Logger LOGGER = LoggerFactory.getLogger(SystemManagerParameters.class);
    private final String[] groupName;
    private final AWSSimpleSystemsManagement ssm;

    public SystemManagerParameters(final String[] groupName, final AWSSimpleSystemsManagement ssm) {
        this.groupName = groupName;
        this.ssm = ssm;
    }

    private SystemManagerParameter convertParameter(final Parameter parameter) {
        return new SystemManagerParameter(parameter);
    }

    private String generatePathName() {
        return Arrays.stream(groupName)
                .map(part -> "/" + part)
                .collect(Collectors.joining());
    }

    @Override
    public String[] getGroupName() {
        return groupName;
    }

    private Optional<Parameter> executeClient(final String name) {
        Parameter parameter = null;
        final String parameterName = generatePathName() + "/" + name;
        try {
            parameter = ssm.getParameter(new GetParameterRequest()
                    .withName(parameterName)
                    .withWithDecryption(true))
                    .getParameter();
        } catch (ParameterNotFoundException nfe) {
            LOGGER.info("Parameter with name {} is not found", parameterName, nfe);
        } catch (SdkBaseException e) {
            LOGGER.warn("Failed to check SSM API", e);
        }
        return Optional.ofNullable(parameter);
    }

    @Override
    public Optional<IParameter> getParameter(final String name) {
        return executeClient(name).map(this::convertParameter);
    }

    @Override
    public Stream<IParameter> getParameters() {
        return StreamSupport.stream(Spliterators.spliteratorUnknownSize(
                new ParameterIterator(generatePathName(), ssm), Spliterator.ORDERED), false)
                .map(this::convertParameter);
    }
}
