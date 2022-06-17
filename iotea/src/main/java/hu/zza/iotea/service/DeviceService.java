package hu.zza.iotea.service;

import hu.zza.iotea.model.dto.DeviceInput;
import hu.zza.iotea.model.dto.DeviceOutput;
import hu.zza.iotea.model.response.Response;
import hu.zza.iotea.model.response.ServiceProblem;
import hu.zza.iotea.model.util.DeviceInputMapper;
import hu.zza.iotea.model.util.DeviceOutputMapper;
import hu.zza.iotea.repository.DeviceRepository;
import java.util.List;
import java.util.Optional;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class DeviceService {
  private DeviceRepository repository;
  private DeviceCommander commander;
  private DeviceInputMapper inMapper;
  private DeviceOutputMapper outMapper;

  public List<DeviceOutput> getAllDevices() {
    return outMapper.toDto(repository.findAll());
  }

  public Optional<DeviceOutput> getDeviceById(Long id) {
    return repository.findById(id).map(outMapper::toDto);
  }

  public DeviceOutput saveDevice(DeviceInput deviceInput) {
    var device = inMapper.toEntity(deviceInput);
    return outMapper.toDto(repository.save(device));
  }

  public Response sendPayload(Long id, String payload) {
    var device = repository.findById(id);

    if (device.isPresent()) {
      return commander.sendPayload(device.get(), payload);
    }
    return new ServiceProblem(id, 0L, "Cannot find Device by id: %s".formatted(id));
  }
}
