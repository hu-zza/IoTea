package hu.zza.bulbman.service;

import hu.zza.bulbman.model.dto.CommandInput;
import hu.zza.bulbman.model.dto.CommandOutput;
import hu.zza.bulbman.model.util.CommandInputMapper;
import hu.zza.bulbman.model.util.CommandOutputMapper;
import hu.zza.bulbman.repository.CommandRepository;
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

  public Optional<CommandOutput> getCommandById(String id) {
    return repository.findById(id).map(outMapper::toDto);
  }

  public CommandOutput saveCommand(CommandInput commandInput) {
    var command = inMapper.toEntity(commandInput);
    return outMapper.toDto(repository.save(command));
  }


}
