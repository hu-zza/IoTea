package hu.zza.iotea.service;

import hu.zza.iotea.model.Device;
import hu.zza.iotea.model.DeviceAddress;
import hu.zza.iotea.model.dto.DeviceInput;
import hu.zza.iotea.model.dto.DeviceOutput;
import hu.zza.iotea.model.exception.EntityNotFoundProblem;
import hu.zza.iotea.model.exception.ServiceProblem;
import hu.zza.iotea.model.util.Numbers;
import hu.zza.iotea.model.util.mapping.DeviceInputMapper;
import hu.zza.iotea.model.util.mapping.DeviceOutputMapper;
import hu.zza.iotea.repository.DeviceRepository;
import java.net.URI;
import java.util.List;
import java.util.Optional;
import java.util.function.Function;
import java.util.function.Supplier;
import java.util.stream.Stream;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.ExampleMatcher;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@AllArgsConstructor
public class DeviceService {
  private DeviceRepository repository;
  private DeviceInputMapper inMapper;
  private DeviceOutputMapper outMapper;

  // simple exposure for hu.zza.iotea.model.util.mapping package
  Device getById(Integer id) {
    return repository
        .findById(id)
        .orElseThrow(
            () -> new EntityNotFoundProblem("There is no Device with id: %d".formatted(id)));
  }

  public Optional<Integer> getIdByUid(String uid) {
    return repository.getIdByUid(uid);
  }

  public Optional<Integer> getIdByName(String name) {
    return repository.getIdByName(name);
  }

  public List<DeviceOutput> getAllDevices() {
    return outMapper.toDto(repository.findAll());
  }

  public List<DeviceOutput> getDevicesByIdentifier(String identifier) {
    return Stream.of(
            getDeviceByFunction(
                repository::findById, Numbers.parseDatabaseIdIfPossible(identifier)),
            getDeviceByFunction(repository::findByName, identifier),
            getDeviceByFunction(repository::findByUid, identifier))
        .filter(Optional::isPresent)
        .map(Optional::get)
        .toList();
  }

  private <T> Optional<DeviceOutput> getDeviceByFunction(
      Function<T, Optional<Device>> func, T param) {
    return getByFunction(func, param).map(outMapper::toDto);
  }

  private <T> Optional<Device> getByFunction(Function<T, Optional<Device>> func, T param) {
    return func.apply(param);
  }

  public Optional<DeviceOutput> getDeviceById(Integer id) {
    return getDeviceByFunction(repository::findById, id);
  }

  public Optional<DeviceOutput> getDeviceByName(String name) {
    return getDeviceByFunction(repository::findByName, name);
  }

  Optional<Device> getByName(String name) {
    return repository.findByName(name);
  }

  public Optional<DeviceOutput> getDeviceByUid(String uid) {
    return getDeviceByFunction(repository::findByUid, uid);
  }

  public List<DeviceOutput> getDevicesByIp(String ip) {
    return outMapper.toDto(getByIp(ip));
  }

  private List<Device> getByIp(String ip) {
    return repository.findAll(
        Example.of(
            new Device(null, null, null, new DeviceAddress(ip), null),
            ExampleMatcher.matchingAll().withIgnoreNullValues()));
  }

  public DeviceOutput saveDevice(DeviceInput deviceInput) {
    return saveDevice(inMapper.toEntity(deviceInput));
  }

  private DeviceOutput saveDevice(Device device) {
    return outMapper.toDto(repository.save(device));
  }

  @Transactional
  public ResponseEntity<DeviceOutput> updateDevice(
      Supplier<Optional<Integer>> idSupplier, DeviceInput input) {
    var optId = idSupplier.get();
    var device = inMapper.toEntity(input);
    optId.ifPresentOrElse(device::setId, () -> device.setId(null));

    if (optId.isPresent()) {
      var id = optId.get();

      if (!repository.existsById(id)) {
        repository.insertWithId(device);
        return ResponseEntity.created(URI.create("/api/devices/id/%d".formatted(id)))
            .body(
                getDeviceById(id)
                    .orElseThrow(() -> new ServiceProblem("Cannot insert %s".formatted(device))));
      }
      return ResponseEntity.ok(saveDevice(device));
    }
    var persisted = saveDevice(device);
    var uri = URI.create("/api/devices/id/%d".formatted(persisted.getId()));
    return ResponseEntity.created(uri).body(persisted);
  }

  public void deleteById(Integer id) {
    repository.deleteById(id);
  }

  @Transactional
  public void deleteByUid(String uid) {
    repository.deleteByUid(uid);
  }

  @Transactional
  public void deleteByName(String name) {
    repository.deleteByName(name);
  }
}
