package hu.zza.bulbman.service;

import hu.zza.bulbman.model.dto.DeviceInput;
import hu.zza.bulbman.model.dto.DeviceOutput;
import hu.zza.bulbman.model.util.DeviceInputMapper;
import hu.zza.bulbman.model.util.DeviceOutputMapper;
import hu.zza.bulbman.repository.DeviceRepository;
import java.util.List;
import java.util.Optional;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class DeviceService {
  private DeviceRepository repository;
  private DeviceInputMapper inMapper;
  private DeviceOutputMapper outMapper;

  public List<DeviceOutput> getAllDevices() {
    return outMapper.toDto(repository.findAll());
  }

  public Optional<DeviceOutput> getDeviceById(String id) {
    return repository.findById(id).map(outMapper::toDto);
  }

  public DeviceOutput saveDevice(DeviceInput deviceInput) {
    var device = inMapper.toEntity(deviceInput);
    return outMapper.toDto(repository.save(device));
  }
}
