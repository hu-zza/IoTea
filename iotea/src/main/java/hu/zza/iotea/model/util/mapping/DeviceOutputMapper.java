package hu.zza.iotea.model.util.mapping;

import hu.zza.iotea.model.Device;
import hu.zza.iotea.model.dto.DeviceOutput;
import java.util.List;
import org.mapstruct.Mapper;

@Mapper(
    componentModel = "spring",
    uses = {DeviceAddressOutputMapper.class})
public interface DeviceOutputMapper {
  DeviceOutput toDto(Device device);

  List<DeviceOutput> toDto(List<Device> devices);
}
