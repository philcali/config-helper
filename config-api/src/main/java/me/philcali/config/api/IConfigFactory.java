package me.philcali.config.api;

import java.util.Optional;

public interface IConfigFactory {
    <T> T create(Class<T> configurationClass, Optional<String> parameterGroup);

    default <T> T create(final Class<T> configurationClass) {
        return create(configurationClass, Optional.empty());
    }
}
