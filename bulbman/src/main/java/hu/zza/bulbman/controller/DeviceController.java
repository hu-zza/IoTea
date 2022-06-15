package hu.zza.bulbman.controller;

import hu.zza.bulbman.model.dto.DeviceInput;
import hu.zza.bulbman.model.dto.DeviceOutput;
import hu.zza.bulbman.model.response.Response;
import hu.zza.bulbman.service.DeviceService;
import java.util.List;
import java.util.Optional;
import javax.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api")
@AllArgsConstructor
public class DeviceController {

  private DeviceService service;

  @GetMapping("/")
  public List<DeviceOutput> getAllDevices() {
    return service.getAllDevices();
  }

  @GetMapping("/{id}")
  public Optional<DeviceOutput> getDeviceById(@PathVariable String id) {
    return service.getDeviceById(id);
  }

  @PutMapping("/{id}")
  public DeviceOutput postDevice(@PathVariable String id, @Valid @RequestBody DeviceInput device) {
    return service.saveDevice(device);
  }

  @PostMapping("/{id}")
  public Response postPayload(@PathVariable String id, @RequestBody String payload) {
    return service.sendPayload(id, payload);
  }
}
