package hu.zza.iotea.model.util;

import hu.zza.iotea.model.Command;
import hu.zza.iotea.model.dto.CommandUpdate;
import java.util.List;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface CommandUpdateMapper {
  @Mapping(target = "note", defaultValue = "")
  Command toEntity(CommandUpdate commandUpdate);

  List<Command> toEntity(List<CommandUpdate> commandUpdates);
}
