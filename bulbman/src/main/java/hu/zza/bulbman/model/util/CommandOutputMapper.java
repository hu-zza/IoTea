package hu.zza.bulbman.model.util;

import hu.zza.bulbman.model.Command;
import hu.zza.bulbman.model.dto.CommandOutput;
import java.util.List;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface CommandOutputMapper {
  @Mapping(target = "note", defaultValue = "")
  CommandOutput toDto(Command command);

  List<CommandOutput> toDto(List<CommandOutput> commandOutputs);
}
