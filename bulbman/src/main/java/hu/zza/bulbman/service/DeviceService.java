package hu.zza.bulbman.service;

import hu.zza.bulbman.model.Device;
import java.util.List;
import org.springframework.stereotype.Service;

@Service
public class DeviceService {
  public <T extends List<U>, U extends Device> T getAllOfType(T container) {

    return container;
  }

}
