package hu.zza.iotea.controller;

import hu.zza.iotea.service.CommandService;
import hu.zza.iotea.service.DeviceService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
@AllArgsConstructor
public class ViewController {
  private DeviceService deviceService;
  private CommandService commandService;

  @GetMapping("/")
  public String getIndex(Model model) {
    model.addAttribute("devices", deviceService.getAllDevices());
    model.addAttribute("commands", commandService.getAllCommands());
    return "index";
  }
}
