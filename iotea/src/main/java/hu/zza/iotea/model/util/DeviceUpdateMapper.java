package hu.zza.iotea.model.util;

import hu.zza.iotea.model.Device;
import hu.zza.iotea.model.DeviceAddress;
import hu.zza.iotea.model.dto.DeviceUpdate;
import java.util.List;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface DeviceUpdateMapper {

  @Mapping(target = "address", source = "deviceUpdate.ip")
  Device toEntity(DeviceUpdate deviceUpdate);

  List<Device> toEntity(List<DeviceUpdate> deviceUpdates);

  default DeviceAddress map(String value) {
    return new DeviceAddress(value);
  }
}
