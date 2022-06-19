package hu.zza.iotea.controller;

import hu.zza.iotea.service.*;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
@AllArgsConstructor
public class ViewController {
  private DeviceService deviceService;
  private CommandService commandService;
  private JobService jobService;

  @GetMapping("/")
  public String getIndex(Model model) {
    model.addAttribute("devices", deviceService.getAllDevices());
    model.addAttribute("commands", commandService.getAllCommands());
    model.addAttribute("jobs", jobService.getAllJobs());
    return "index";
  }
}
