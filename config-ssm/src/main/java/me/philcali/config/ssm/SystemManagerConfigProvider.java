package me.philcali.config.ssm;

import java.util.Arrays;
import java.util.stream.Collectors;

import com.amazonaws.services.simplesystemsmanagement.AWSSimpleSystemsManagement;
import com.amazonaws.services.simplesystemsmanagement.AWSSimpleSystemsManagementClientBuilder;

import me.philcali.config.api.IConfigProvider;
import me.philcali.config.api.IParameters;
import me.philcali.config.api.exception.ConfigProvisionException;

public class SystemManagerConfigProvider implements IConfigProvider {
    private final AWSSimpleSystemsManagement ssm;

    public SystemManagerConfigProvider(final AWSSimpleSystemsManagement ssm) {
        this.ssm = ssm;
    }

    // Constructor intended to be used by the SPI
    public SystemManagerConfigProvider() {
        this(AWSSimpleSystemsManagementClientBuilder.defaultClient());
    }

    @Override
    public IParameters get(final String ... groupName) throws ConfigProvisionException {
        // parts.addPart("Prod").addPart("Application") == "/Prod/Application/{groupName}"
        return new SystemManagerParameters(Arrays.stream(groupName)
                .map(part -> "/" + part)
                .collect(Collectors.joining()), ssm);
    }
}
