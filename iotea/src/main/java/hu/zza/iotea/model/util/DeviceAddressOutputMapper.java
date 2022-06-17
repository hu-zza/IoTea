package hu.zza.iotea.model.util;

import hu.zza.iotea.model.DeviceAddress;
import hu.zza.iotea.model.dto.DeviceAddressOutput;
import java.util.List;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface DeviceAddressOutputMapper {
  DeviceAddressOutput toDto(DeviceAddress deviceAddress);

  List<DeviceAddressOutput> toDto(List<DeviceAddress> deviceAddresses);
}
