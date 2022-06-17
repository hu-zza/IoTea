package hu.zza.bulbman.controller;

import hu.zza.bulbman.model.dto.CommandInput;
import hu.zza.bulbman.model.dto.CommandOutput;
import hu.zza.bulbman.service.CommandService;
import java.util.List;
import java.util.Optional;
import javax.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/commands")
@AllArgsConstructor
public class CommandController {
  private CommandService service;

  @GetMapping
  public List<CommandOutput> getAllCommands() {
    return service.getAllCommands();
  }

  @GetMapping("/{id}")
  public Optional<CommandOutput> getDeviceById(@PathVariable String id) {
    return service.getCommandById(id);
  }

  @PutMapping("/{id}")
  public CommandOutput postDevice(
      @PathVariable String id, @Valid @RequestBody CommandInput command) {
    return service.saveCommand(command);
  }
}
