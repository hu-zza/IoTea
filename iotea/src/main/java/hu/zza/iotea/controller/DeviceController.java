package hu.zza.iotea.controller;

import hu.zza.iotea.model.dto.DeviceInput;
import hu.zza.iotea.model.dto.DeviceOutput;
import hu.zza.iotea.model.exception.EntityNotFoundProblem;
import hu.zza.iotea.service.DeviceService;
import java.util.List;
import java.util.Optional;
import javax.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/devices")
@AllArgsConstructor
public class DeviceController {

  private DeviceService service;

  @GetMapping
  public List<DeviceOutput> getAllDevices() {
    return service.getAllDevices();
  }

  @GetMapping("/{identifier}")
  public List<DeviceOutput> getDevicesByIdentifier(@PathVariable String identifier) {
    return service.getDevicesByIdentifier(identifier);
  }

  @GetMapping("/id/{id}")
  public DeviceOutput getDeviceById(@PathVariable Integer id) {
    return service
        .getDeviceById(id)
        .orElseThrow(
            () -> new EntityNotFoundProblem("There is no Device with id: %d".formatted(id)));
  }

  @GetMapping("/uid/{uid}")
  public DeviceOutput getDeviceByUid(@PathVariable String uid) {
    return service
        .getDeviceByUid(uid)
        .orElseThrow(
            () -> new EntityNotFoundProblem("There is no Device with uid: %s".formatted(uid)));
  }

  @GetMapping("/name/{name}")
  public DeviceOutput getDeviceByName(@PathVariable String name) {
    return service
        .getDeviceByName(name)
        .orElseThrow(
            () -> new EntityNotFoundProblem("There is no Device with name: %s".formatted(name)));
  }

  @GetMapping("/ip/{ip}")
  public List<DeviceOutput> getDevicesByIp(@PathVariable String ip) {
    return service.getDevicesByIp(ip);
  }

  @PostMapping
  @ResponseStatus(HttpStatus.CREATED)
  public DeviceOutput postDevice(@Valid @RequestBody DeviceInput device) {
    return service.saveDevice(device);
  }

  @PutMapping("/id/{id}")
  public DeviceOutput putDeviceToId(
      @PathVariable Integer id, @Valid @RequestBody DeviceInput device) {
    return service.updateDevice(() -> Optional.of(id), device);
  }

  @PutMapping("/uid/{uid}")
  public DeviceOutput putDeviceToUid(
      @PathVariable String uid, @Valid @RequestBody DeviceInput device) {
    device.setUid(uid);
    return service.updateDevice(() -> service.getIdByUid(uid), device);
  }

  @PutMapping("/name/{name}")
  public DeviceOutput putDeviceToName(
      @PathVariable String name, @Valid @RequestBody DeviceInput device) {
    device.setName(name);
    return service.updateDevice(() -> service.getIdByName(name), device);
  }

  @DeleteMapping("/id/{id}")
  @ResponseStatus(HttpStatus.NO_CONTENT)
  public void deleteDeviceById(@PathVariable Integer id) {
    service.deleteById(id);
  }

  @DeleteMapping("/uid/{uid}")
  @ResponseStatus(HttpStatus.NO_CONTENT)
  public void deleteDeviceByUid(@PathVariable String uid) {
    service.deleteByUid(uid);
  }

  @DeleteMapping("/name/{name}")
  @ResponseStatus(HttpStatus.NO_CONTENT)
  public void deleteDeviceByName(@PathVariable String name) {
    service.deleteByName(name);
  }
}
