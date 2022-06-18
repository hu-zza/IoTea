package hu.zza.iotea.model.util;

import hu.zza.iotea.model.Job;
import hu.zza.iotea.model.dto.JobOutput;
import java.util.List;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface JobOutputMapper {
  JobOutput toDto(Job job);
  List<JobOutput> toDto(List<Job> jobs);
}
