package hu.zza.bulbman.controller;

import hu.zza.bulbman.model.xiaomi.yeelight.YeelightDevice;
import hu.zza.bulbman.service.DeviceService;
import java.util.ArrayList;
import java.util.List;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/devices")
@AllArgsConstructor
public class DeviceController {

  private DeviceService service;

  @GetMapping
  public List<YeelightDevice> yeelightDevices() {
    return service.getAllOfType(new ArrayList<>());
  }

}
