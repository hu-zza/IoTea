package hu.zza.iotea.model.util.mapping;

import hu.zza.iotea.model.Device;
import hu.zza.iotea.model.DeviceAddress;
import hu.zza.iotea.model.dto.DeviceInput;
import java.util.List;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface DeviceInputMapper extends InputMapper<Device, DeviceInput> {

  @Mapping(target = "id", ignore = true)
  @Mapping(target = "address", source = "deviceInput.ip")
  Device toEntity(DeviceInput deviceInput);

  List<Device> toEntity(List<DeviceInput> deviceInputs);

  default DeviceAddress map(String value) {
    return new DeviceAddress(value);
  }
}
