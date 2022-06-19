package hu.zza.iotea.service;

import hu.zza.iotea.model.*;
import hu.zza.iotea.model.util.mapping.DoIgnore;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class ModelService {
  private DeviceService deviceService;
  private CommandService commandService;
  @DoIgnore
  protected Device getDevice(Integer id) {
    return deviceService.getById(id);
  }

  @DoIgnore
  protected Command getCommand(Integer id) {
    return commandService.getById(id);
  }
}
