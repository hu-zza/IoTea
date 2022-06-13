package hu.zza.bulbman.model.util;

import hu.zza.bulbman.model.Device;
import hu.zza.bulbman.model.dto.DeviceOutput;
import java.util.List;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface DeviceOutputMapper {
  DeviceOutput toDto(Device device);
  List<DeviceOutput> toDto(List<Device> devices);
}
