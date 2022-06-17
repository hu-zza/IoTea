package hu.zza.iotea.controller;

import hu.zza.iotea.model.dto.DeviceInput;
import hu.zza.iotea.model.dto.DeviceOutput;
import hu.zza.iotea.model.response.Response;
import hu.zza.iotea.service.DeviceService;
import java.util.List;
import java.util.Optional;
import javax.validation.Valid;
import lombok.AllArgsConstructor;
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

  @GetMapping("/{id}")
  public Optional<DeviceOutput> getDeviceById(@PathVariable Integer id) {
    return service.getDeviceById(id);
  }

  @PutMapping("/{id}")
  public DeviceOutput postDevice(@PathVariable Integer id, @Valid @RequestBody DeviceInput device) {
    return service.saveDevice(device);
  }

  @PostMapping("/{id}")
  public Response postPayload(@PathVariable Integer id, @RequestBody String payload) {
    return service.sendPayload(id, payload);
  }
}
