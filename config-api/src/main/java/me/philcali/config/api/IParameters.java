package me.philcali.config.api;

import java.util.Optional;
import java.util.stream.Stream;

public interface IParameters {
    String getGroup();
    Optional<IParameter> getParameter(String name);
    Stream<IParameter> getParameters();
}
