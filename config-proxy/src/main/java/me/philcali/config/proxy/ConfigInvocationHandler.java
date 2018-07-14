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
import me.philcali.config.api.IConfigProvider;
import me.philcali.config.proxy.resolver.ITypeResolver;

public class ConfigInvocationHandler implements InvocationHandler {
    private static final Logger LOGGER = LoggerFactory.getLogger(ConfigInvocationHandler.class);

    private final IConfigProvider provider;
    private final ITypeResolver resolver;
    private final Optional<String> parameterGroup;

    public ConfigInvocationHandler(
            final IConfigProvider provider,
            final ITypeResolver resolver,
            final Optional<String> parameterGroup) {
        this.provider = provider;
        this.resolver = resolver;
        this.parameterGroup = parameterGroup;
    }

    @Override
    public Object invoke(final Object proxy, final Method method, final Object[] args) throws Throwable {
        final String groupName = parameterGroup.orElseGet(() -> Optional.ofNullable(method.getDeclaringClass().getAnnotation(ParameterGroup.class))
                .map(ParameterGroup::value)
                .orElseGet(method.getDeclaringClass()::getSimpleName));
        final Parameter parameter = method.getAnnotation(Parameter.class);
        final String parameterName = Optional.ofNullable(parameter)
                .map(Parameter::value)
                .filter(name -> !name.isEmpty())
                .orElseGet(() -> method.getName().replaceAll("^get", ""));
        final IParameterRecovery recovery = Optional.ofNullable(parameter)
                .map(Parameter::emptyRecovery)
                .flatMap(this::instantiateRecovery)
                .orElseGet(ErrorParameterRecovery::new);
        // throws ConfigProvisionException
        return provider.get(groupName).getParameter(parameterName)
                .map(param -> resolver.resolve(param.getValue(), method.getReturnType(), method.getGenericReturnType()))
                .orElseGet(() -> recovery.recover(groupName, parameterName));
    }

    public Optional<IParameterRecovery> instantiateRecovery(final Class<? extends IParameterRecovery> recoveryClass) {
        IParameterRecovery recovery = null;
        try {
            recovery = recoveryClass.newInstance();
        } catch (InstantiationException | IllegalAccessException e) {
            LOGGER.error("Failed to instantiate recovery class {}", recoveryClass, e);
        }
        return Optional.ofNullable(recovery);
    }
}
