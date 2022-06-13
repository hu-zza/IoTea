package hu.zza.bulbman.model.util;

import hu.zza.bulbman.model.Device;
import hu.zza.bulbman.model.DeviceAddress;
import hu.zza.bulbman.model.dto.DeviceInput;
import java.util.List;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface DeviceInputMapper {

  @Mapping(target = "address", source = "deviceInput.ip")
  Device toEntity(DeviceInput deviceInput);
  List<Device> toEntity(List<DeviceInput> deviceInputs);

  default DeviceAddress map(String value) {
    return new DeviceAddress(value);
  }
}
