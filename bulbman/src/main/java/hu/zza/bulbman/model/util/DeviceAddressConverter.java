package hu.zza.bulbman.model.util;

import hu.zza.bulbman.model.DeviceAddress;
import javax.persistence.AttributeConverter;
import javax.persistence.Converter;

@Converter
public class DeviceAddressConverter implements AttributeConverter<DeviceAddress, String> {

  @Override
  public String convertToDatabaseColumn(DeviceAddress address) {
    return null;
  }

  @Override
  public DeviceAddress convertToEntityAttribute(String address) {
    // ............ ?!
    return null;
  }
}
