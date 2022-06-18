package hu.zza.iotea.service;

import hu.zza.iotea.model.Command;
import hu.zza.iotea.model.dto.CommandInput;
import hu.zza.iotea.model.dto.CommandOutput;
import hu.zza.iotea.model.util.*;
import hu.zza.iotea.repository.CommandRepository;
import java.util.List;
import java.util.Optional;
import java.util.function.Function;
import java.util.function.Supplier;
import java.util.stream.Stream;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@AllArgsConstructor
public class CommandService {
  private CommandRepository repository;
  private CommandInputMapper inMapper;
  private CommandOutputMapper outMapper;

  public Optional<Integer> getIdByName(String name) {
    return repository.getIdByName(name);
  }

  public List<CommandOutput> getAllCommands() {
    return outMapper.toDto(repository.findAll());
  }

  public List<CommandOutput> getCommandsByIdentifier(String identifier) {
    return Stream.of(
            getCommandByFunction(
                repository::findById, NumberUtil.parseDatabaseIdIfPossible(identifier)),
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
  public CommandOutput updateCommand(Supplier<Optional<Integer>> idSupplier, CommandInput update) {
    var optId = idSupplier.get();
    var command = inMapper.toEntity(update);
    optId.ifPresentOrElse(command::setId, () -> command.setId(null));

    if (optId.isPresent()) {
      var id = optId.get();

      if (!repository.existsById(id)) {
        repository.insertWithId(command);
        return getCommandById(id).orElseThrow();
      }
    }
    return saveCommand(command);
  }

  @Transactional
  public void deleteById(Integer id) {
    repository.deleteById(id);
  }

  public void deleteByName(String name) {
    repository.deleteByName(name);
  }
}
