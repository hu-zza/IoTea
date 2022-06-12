package hu.zza.bulbman.service;

import hu.zza.bulbman.model.Device;
import hu.zza.bulbman.repository.DeviceRepository;
import java.util.List;
import java.util.Optional;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class DeviceService {
  private DeviceRepository repository;

  public List<Device> getAllDevices() {
    return repository.findAll();
  }
  public Optional<Device> getDeviceById(String id) {
    return repository.findById(id);
  }
  public Device saveDevice(Device addressable) {
    return repository.save(addressable);
  }
}
