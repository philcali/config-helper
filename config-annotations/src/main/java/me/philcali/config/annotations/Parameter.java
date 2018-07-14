package me.philcali.config.annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import me.philcali.config.annotations.recovery.ErrorParameterRecovery;
import me.philcali.config.annotations.recovery.IParameterRecovery;

@Retention(RetentionPolicy.RUNTIME)
@Target({ ElementType.FIELD, ElementType.METHOD })
public @interface Parameter {
    String value() default "";
    Class<? extends IParameterRecovery> emptyRecovery() default ErrorParameterRecovery.class;
}
