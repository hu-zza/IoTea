package hu.zza.bulbman.controller;

import hu.zza.bulbman.service.DeviceService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
@AllArgsConstructor
public class ViewController {
  private DeviceService service;
  @GetMapping("/")
  public String getIndex(Model model) {
    model.addAttribute("bulbs", service.getAllDevices());
    return "index";
  }
}
