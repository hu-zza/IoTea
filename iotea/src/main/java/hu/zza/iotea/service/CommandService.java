package hu.zza.iotea.service;

import hu.zza.iotea.model.dto.CommandInput;
import hu.zza.iotea.model.dto.CommandOutput;
import hu.zza.iotea.model.util.CommandInputMapper;
import hu.zza.iotea.model.util.CommandOutputMapper;
import hu.zza.iotea.repository.CommandRepository;
import java.util.List;
import java.util.Optional;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class CommandService {
  private CommandRepository repository;
  private CommandInputMapper inMapper;
  private CommandOutputMapper outMapper;

  public List<CommandOutput> getAllCommands() {
    return outMapper.toDto(repository.findAll());
  }

  public Optional<CommandOutput> getCommandById(Long id) {
    return repository.findById(id).map(outMapper::toDto);
  }

  public CommandOutput saveCommand(CommandInput commandInput) {
    var command = inMapper.toEntity(commandInput);
    return outMapper.toDto(repository.save(command));
  }
}
