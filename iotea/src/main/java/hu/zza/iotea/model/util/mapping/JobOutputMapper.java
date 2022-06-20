package hu.zza.iotea.model.util.mapping;

import hu.zza.iotea.model.Job;
import hu.zza.iotea.model.dto.JobOutput;
import java.util.List;
import org.mapstruct.Mapper;

@Mapper(
    componentModel = "spring",
    uses = {DeviceOutputMapper.class, CommandOutputMapper.class})
public interface JobOutputMapper extends OutputMapper<Job, JobOutput> {

  JobOutput toDto(Job job);

  List<JobOutput> toDto(List<Job> jobs);
}
