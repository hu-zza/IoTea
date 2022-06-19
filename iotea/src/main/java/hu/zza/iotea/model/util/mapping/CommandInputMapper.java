package hu.zza.iotea.model.util.mapping;

import hu.zza.iotea.model.Command;
import hu.zza.iotea.model.dto.CommandInput;
import java.util.List;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface CommandInputMapper {
  @Mapping(target = "id", ignore = true)
  @Mapping(target = "note", defaultValue = "")
  Command toEntity(CommandInput commandInput);

  List<Command> toEntity(List<CommandInput> commandInputs);
}
