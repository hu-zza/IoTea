package hu.zza.iotea.service;

import hu.zza.iotea.model.*;
import hu.zza.iotea.model.dto.*;
import hu.zza.iotea.model.response.Response;
import hu.zza.iotea.model.response.ServiceProblem;
import hu.zza.iotea.model.util.*;
import hu.zza.iotea.repository.DeviceCrudRepository;
import hu.zza.iotea.repository.DeviceRepository;
import java.util.List;
import java.util.Optional;
import java.util.function.*;
import java.util.stream.Stream;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.ExampleMatcher;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@AllArgsConstructor
public class DeviceService {
  private DeviceRepository repository;
  private DeviceCrudRepository crudRepository;
  private DeviceCommander commander;
  private DeviceInputMapper inMapper;
  private DeviceUpdateMapper updateMapper;
  private DeviceOutputMapper outMapper;

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
                repository::findById, NumberUtil.parseDatabaseIdIfPossible(identifier)),
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

  public Optional<DeviceOutput> getDeviceByUid(String uid) {
    return getDeviceByFunction(repository::findByUid, uid);
  }

  public List<DeviceOutput> getDevicesByIp(String ip) {
    return outMapper.toDto(getByIp(ip));
  }

  private List<Device> getByIp(String ip) {
    return repository.findAll(
        Example.of(
            new Device(null, null, null, new DeviceAddress(ip), null, false),
            ExampleMatcher.matchingAll().withIgnoreNullValues()));
  }

  public DeviceOutput saveDevice(DeviceInput deviceInput) {
    return saveDevice(inMapper.toEntity(deviceInput));
  }

  private DeviceOutput saveDevice(Device device) {
    return outMapper.toDto(repository.save(device));
  }

  @Transactional
  public DeviceOutput updateDevice(Supplier<Optional<Integer>> idSupplier, DeviceUpdate update) {
    var id = idSupplier.get();
    var device = updateMapper.toEntity(update);

    if (id.isPresent()) {
      // CRUD
      //device.setId(id.get());
      //return outMapper.toDto(crudRepository.save(device));

      // Persistable class
      //var persistable = PersistableDevice.of(device);
      //persistable.setId(id.get());
      //return saveDevice(persistable);

      // Persistable
      device.setId(id.get());
      //device.setNew(true);
      return saveDevice(device);

      // ugly
      //update.setId(id.get());
      //repository.saveUnderId(update);
      //return getDeviceById(id.get()).orElseThrow();
    }
    return saveDevice(device);
  }

  public void deleteById(Integer id) {
    repository.deleteById(id);
  }

  public void deleteByUid(String uid) {
    repository.deleteByUid(uid);
  }

  public void deleteByName(String name) {
    repository.deleteByName(name);
  }

  public Response sendPayload(Integer id, String payload) {
    var device = repository.findById(id);

    if (device.isPresent()) {
      return commander.sendPayload(device.get(), payload);
    }
    return new ServiceProblem(id, 0, "Cannot find Device by id: %s".formatted(id));
  }
}
