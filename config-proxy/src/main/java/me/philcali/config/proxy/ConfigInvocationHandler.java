package me.philcali.config.proxy;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import me.philcali.config.annotations.Parameter;
import me.philcali.config.annotations.ParameterGroup;
import me.philcali.config.annotations.recovery.ErrorParameterRecovery;
import me.philcali.config.annotations.recovery.IParameterRecovery;
import me.philcali.config.api.IConfigFactory;

public class ConfigInvocationHandler implements InvocationHandler {
    private static final Logger LOGGER = LoggerFactory.getLogger(ConfigInvocationHandler.class);

    private final ConfigProxyFactoryOptions options;
    private final Optional<String> parameterGroup;
    private final String[] parameters;
    private final IConfigFactory factory;

    public ConfigInvocationHandler(final IConfigFactory factory, final ConfigProxyFactoryOptions options,
            final Optional<String> parameterGroup, final String... parameters) {
        this.factory = factory;
        this.options = options;
        this.parameterGroup = parameterGroup;
        this.parameters = parameters;
    }

    @Override
    public Object invoke(final Object proxy, final Method method, final Object[] args) throws Throwable {
        final String groupName = parameterGroup
                .orElseGet(() -> Optional.ofNullable(method.getDeclaringClass().getAnnotation(ParameterGroup.class))
                        .map(ParameterGroup::value).orElseGet(method.getDeclaringClass()::getSimpleName));
        final Parameter parameter = method.getAnnotation(Parameter.class);
        final String parameterName = Optional.ofNullable(parameter).map(Parameter::value)
                .filter(name -> !name.isEmpty()).orElseGet(() -> method.getName().replaceAll("^get", ""));
        final IParameterRecovery recovery = Optional.ofNullable(parameter).map(Parameter::emptyRecovery)
                .flatMap(this::instantiateRecovery).orElseGet(ErrorParameterRecovery::new);
        // throws ConfigProvisionException
        // TODO: handle Maps... that's a getParameters call
        if (method.getReturnType().isInterface() && !options.getTypeResolver().isApplicable(method.getReturnType())) {
            return factory.create(method.getReturnType(), Optional.of(groupName), append(parameterName));
        } else {
            return options.getConfigProvider().get(prepend(options.getGroupPrefix().replace(groupName)))
                    .getParameter(parameterName)
                    .map(param -> options.getTypeResolver().resolve(param.getValue(), method.getReturnType(),
                            method.getGenericReturnType()))
                    .orElseGet(() -> recovery.recover(groupName, append(parameterName)));
        }
    }

    private String[] append(final String parameterName) {
        final String[] appended = new String[parameters.length + 1];
        int i;
        for (i = 0; i < parameters.length; i++) {
            appended[i] = parameters[i];
        }
        appended[i] = parameterName;
        return appended;
    }

    private String[] prepend(final String... groupNames) {
        final String[] prepended = new String[parameters.length + groupNames.length];
        int i;
        for (i = 0; i < groupNames.length; i++) {
            prepended[i] = groupNames[i];
        }
        for (; i < (groupNames.length + parameters.length); i++) {
            prepended[i] = parameters[i - groupNames.length];
        }
        return prepended;
    }

    private Optional<IParameterRecovery> instantiateRecovery(final Class<? extends IParameterRecovery> recoveryClass) {
        IParameterRecovery recovery = null;
        try {
            recovery = recoveryClass.newInstance();
        } catch (InstantiationException | IllegalAccessException e) {
            LOGGER.error("Failed to instantiate recovery class {}", recoveryClass, e);
        }
        return Optional.ofNullable(recovery);
    }
}
