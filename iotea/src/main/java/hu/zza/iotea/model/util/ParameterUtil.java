package hu.zza.iotea.model.util;

import java.util.Arrays;

public interface ParameterUtil {
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
    return Arrays.stream(parameters).map(ParameterUtil::prepareParameter).toArray();
  }

  static Object prepareParameter(String parameter) {
    try {
      return Integer.valueOf(parameter);
    } catch (NumberFormatException ignored) {
    }
    return parameter;
  }
}
