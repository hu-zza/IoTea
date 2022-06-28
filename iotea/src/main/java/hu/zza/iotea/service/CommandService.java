package hu.zza.iotea.service;

import hu.zza.iotea.model.Command;
import hu.zza.iotea.model.dto.CommandInput;
import hu.zza.iotea.model.dto.CommandOutput;
import hu.zza.iotea.model.exception.EntityNotFoundProblem;
import hu.zza.iotea.model.exception.ServiceProblem;
import hu.zza.iotea.model.util.Numbers;
import hu.zza.iotea.model.util.mapping.CommandInputMapper;
import hu.zza.iotea.model.util.mapping.CommandOutputMapper;
import hu.zza.iotea.repository.CommandRepository;
import java.net.URI;
import java.util.List;
import java.util.Optional;
import java.util.function.Function;
import java.util.function.Supplier;
import java.util.stream.Stream;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@AllArgsConstructor
public class CommandService {
  private CommandRepository repository;
  private CommandInputMapper inMapper;
  private CommandOutputMapper outMapper;

  // simple exposure for hu.zza.iotea.model.util.mapping package
  Command getById(Integer id) {
    return repository
        .findById(id)
        .orElseThrow(
            () -> new EntityNotFoundProblem("There is no Command with id: %d".formatted(id)));
  }

  public Optional<Integer> getIdByName(String name) {
    return repository.getIdByName(name);
  }

  public List<CommandOutput> getAllCommands() {
    return outMapper.toDto(repository.findAll());
  }

  public List<CommandOutput> getCommandsByIdentifier(String identifier) {
    return Stream.of(
            getCommandByFunction(
                repository::findById, Numbers.parseDatabaseIdIfPossible(identifier)),
            getCommandByFunction(repository::findByName, identifier))
        .filter(Optional::isPresent)
        .map(Optional::get)
        .toList();
  }

  private <T> Optional<CommandOutput> getCommandByFunction(
      Function<T, Optional<Command>> func, T param) {
    return getByFunction(func, param).map(outMapper::toDto);
  }

  private <T> Optional<Command> getByFunction(Function<T, Optional<Command>> func, T param) {
    return func.apply(param);
  }

  public Optional<CommandOutput> getCommandById(Integer id) {
    return getCommandByFunction(repository::findById, id);
  }

  public Optional<CommandOutput> getCommandByName(String name) {
    return getCommandByFunction(repository::findByName, name);
  }

  Optional<Command> getByName(String name) {
    return repository.findByName(name);
  }

  public CommandOutput saveCommand(CommandInput commandInput) {
    return saveCommand(inMapper.toEntity(commandInput));
  }

  private CommandOutput saveCommand(Command command) {
    return outMapper.toDto(repository.save(command));
  }

  @Transactional
  public ResponseEntity<CommandOutput> updateCommand(
      Supplier<Optional<Integer>> idSupplier, CommandInput update) {
    var optId = idSupplier.get();
    var command = inMapper.toEntity(update);
    optId.ifPresentOrElse(command::setId, () -> command.setId(null));

    if (optId.isPresent()) {
      var id = optId.get();

      if (!repository.existsById(id)) {
        repository.insertWithId(command);
        return ResponseEntity.created(URI.create("/api/commands/id/%d".formatted(id)))
            .body(
                getCommandById(id)
                    .orElseThrow(() -> new ServiceProblem("Cannot insert %s".formatted(command))));
      }
    }
    return ResponseEntity.ok(saveCommand(command));
  }

  public void deleteById(Integer id) {
    repository.deleteById(id);
  }

  @Transactional
  public void deleteByName(String name) {
    repository.deleteByName(name);
  }
}
