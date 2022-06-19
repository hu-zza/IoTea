package hu.zza.iotea.model.util.mapping;

import hu.zza.iotea.model.DeviceAddress;
import hu.zza.iotea.model.dto.DeviceAddressOutput;
import java.net.InetAddress;
import java.util.List;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface DeviceAddressOutputMapper {

  DeviceAddressOutput toDto(DeviceAddress deviceAddress);

  List<DeviceAddressOutput> toDto(List<DeviceAddress> deviceAddresses);

  default String map(InetAddress value) {
    return value.getHostAddress();
  }
}
