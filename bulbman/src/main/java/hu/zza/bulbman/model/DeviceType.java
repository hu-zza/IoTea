package hu.zza.bulbman.model;

import hu.zza.bulbman.model.util.ConverterException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public enum DeviceType {
  XIAOMI_YEELIGHT;

  private static final Logger logger = LoggerFactory.getLogger(DeviceType.class);

  public static DeviceType parse(String deviceType) {
    try {
      return DeviceType.valueOf(
          deviceType.toUpperCase().replaceAll("\\W+", "_"));
    } catch (IllegalArgumentException exception) {
      throw new ConverterException(
          "Cannot parse '%s' as DeviceType. Available device types are: %s (Case-insensitive, regex cleanup: '\\W+' -> '_')"
              .formatted(deviceType, DeviceType.values()),
          exception);
    }
  }
}
