package hu.zza.iotea.model.util.mapping;

import hu.zza.iotea.model.Job;
import hu.zza.iotea.model.dto.JobInput;
import java.util.List;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(
    componentModel = "spring",
    uses = {ModelProvider.class})
public interface JobInputMapper extends InputMapper<Job, JobInput>{
  @Mapping(target = "id", ignore = true)
  @Mapping(target = "device", source = "deviceId")
  @Mapping(target = "command", source = "commandId")
  @Mapping(target = "result", ignore = true)
  Job toEntity(JobInput jobInput);

  List<Job> toEntity(List<JobInput> jobInputs);
}
