package hu.zza.bulbman.model.util;

import hu.zza.bulbman.model.Device;
import hu.zza.bulbman.model.dto.DeviceOutput;
import java.util.List;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface DeviceOutputMapper {
  @Mapping(target = "ip", source = "device.address")
  DeviceOutput toDto(Device device);
  List<DeviceOutput> toDto(List<Device> devices);
}
