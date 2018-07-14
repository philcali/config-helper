package me.philcali.config.api;

import me.philcali.config.api.exception.ConfigProvisionException;

public interface IConfigProvider {
    IParameters get(String groupName) throws ConfigProvisionException;
}
