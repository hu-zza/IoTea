package hu.zza.bulbman.model.util;

import hu.zza.bulbman.model.Command;
import hu.zza.bulbman.model.dto.CommandInput;
import java.util.List;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface CommandInputMapper {
  @Mapping(target = "note", defaultValue = "")
  Command toEntity(CommandInput commandInput);

  List<Command> toEntity(List<CommandInput> commandInputs);
}
