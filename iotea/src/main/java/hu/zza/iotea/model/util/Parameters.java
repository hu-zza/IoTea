package hu.zza.iotea.model.util;

import java.util.Arrays;

public interface Parameters {
  String DELIMITER = "[^\\w.-]";

  static Object[] prepareParameters(String raw) {
    if (raw == null) {
      return new Object[0];
    }
    return prepareParameters(raw.split(DELIMITER));
  }

  static Object[] prepareParameters(String... parameters) {
    if (parameters == null) {
      return new Object[0];
    }
    return Arrays.stream(parameters).map(Parameters::prepareParameter).toArray();
  }

  static Object prepareParameter(String parameter) {
    try {
      return Integer.valueOf(parameter);
    } catch (NumberFormatException ignored) {
    }
    return parameter;
  }
}
