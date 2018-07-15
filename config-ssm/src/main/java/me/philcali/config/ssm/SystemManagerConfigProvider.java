package me.philcali.config.ssm;

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
        return new SystemManagerParameters(groupName, ssm);
    }
}
