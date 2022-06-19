package hu.zza.iotea.model.util.mapping;

import hu.zza.iotea.model.Command;
import hu.zza.iotea.model.dto.CommandOutput;
import java.util.List;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface CommandOutputMapper {
  @Mapping(target = "note", defaultValue = "")
  CommandOutput toDto(Command command);

  List<CommandOutput> toDto(List<Command> commandOutputs);
}
