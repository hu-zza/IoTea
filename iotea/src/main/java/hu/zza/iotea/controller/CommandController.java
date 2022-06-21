package hu.zza.iotea.controller;

import hu.zza.iotea.model.dto.CommandInput;
import hu.zza.iotea.model.dto.CommandOutput;
import hu.zza.iotea.model.exception.EntityNotFoundProblem;
import hu.zza.iotea.service.CommandService;
import java.util.List;
import java.util.Optional;
import javax.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
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

  @GetMapping("/{identifier}")
  public List<CommandOutput> getCommandsByIdentifier(@PathVariable String identifier) {
    return service.getCommandsByIdentifier(identifier);
  }

  @GetMapping("/id/{id}")
  public CommandOutput getCommandById(@PathVariable Integer id) {
    return service
        .getCommandById(id)
        .orElseThrow(
            () -> new EntityNotFoundProblem("There is no Command with id: %d".formatted(id)));
  }

  @GetMapping("/name/{name}")
  public CommandOutput getCommandByName(@PathVariable String name) {
    return service
        .getCommandByName(name)
        .orElseThrow(
            () -> new EntityNotFoundProblem("There is no Command with name: %s".formatted(name)));
  }

  @PostMapping
  public CommandOutput postDevice(@Valid @RequestBody CommandInput command) {
    return service.saveCommand(command);
  }

  @PutMapping("/id/{id}")
  public CommandOutput putDeviceToId(
      @PathVariable Integer id, @Valid @RequestBody CommandInput command) {
    return service.updateCommand(() -> Optional.of(id), command);
  }

  @PutMapping("/name/{name}")
  public CommandOutput putDeviceToName(
      @PathVariable String name, @Valid @RequestBody CommandInput command) {
    command.setName(name);
    return service.updateCommand(() -> service.getIdByName(name), command);
  }

  @DeleteMapping("/id/{id}")
  @ResponseStatus(HttpStatus.NO_CONTENT)
  public void deleteCommandById(@PathVariable Integer id) {
    service.deleteById(id);
  }

  @DeleteMapping("/name/{name}")
  @ResponseStatus(HttpStatus.NO_CONTENT)
  public void deleteCommandByName(@PathVariable String name) {
    service.deleteByName(name);
  }
}
