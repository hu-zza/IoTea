package hu.zza.iotea.model.util.mapping;

import hu.zza.iotea.model.Command;
import hu.zza.iotea.model.Device;
import hu.zza.iotea.service.*;
import org.springframework.stereotype.Component;

@Component
class ModelProvider extends ModelService {
  public ModelProvider(DeviceService deviceService, CommandService commandService) {
    super(deviceService, commandService);
  }

  Device getDeviceReference(Integer id) {
    return getDevice(id);
  }

  Command getCommandReference(Integer id) {
    return getCommand(id);
  }
}
