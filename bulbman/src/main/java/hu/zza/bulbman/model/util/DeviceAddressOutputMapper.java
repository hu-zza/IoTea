package hu.zza.bulbman.model.util;

import hu.zza.bulbman.model.DeviceAddress;
import hu.zza.bulbman.model.dto.DeviceAddressOutput;
import java.util.List;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface DeviceAddressOutputMapper {
  DeviceAddressOutput toDto(DeviceAddress deviceAddress);
  List<DeviceAddressOutput> toDto(List<DeviceAddress> deviceAddresses);
}
