package hu.zza.bulbman.controller;

import hu.zza.bulbman.model.Device;
import hu.zza.bulbman.service.DeviceService;
import java.util.List;
import java.util.Optional;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api")
@AllArgsConstructor
public class DeviceController {

  private DeviceService service;

  @GetMapping("/")
  public List<Device> getAllDevices() {
    return service.getAllDevices();
  }

  @GetMapping("/{id}")
  public Optional<Device> getDeviceById(@PathVariable String id) {
    return service.getDeviceById(id);
  }

  @PutMapping("/{id}")
  public Device postDevice(@PathVariable String id, @RequestBody Device device) {
    return service.saveDevice(device);
  }
}
