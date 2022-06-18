package hu.zza.iotea.service;

import hu.zza.iotea.model.Job;
import hu.zza.iotea.model.dto.JobOutput;
import hu.zza.iotea.model.util.JobOutputMapper;
import hu.zza.iotea.model.util.NumberUtil;
import hu.zza.iotea.repository.JobRepository;
import java.util.List;
import java.util.Optional;
import java.util.function.Function;
import java.util.stream.Stream;
import org.springframework.stereotype.Service;

@Service
public class JobService {
  private JobRepository repository;
  private JobOutputMapper mapper;

  public List<JobOutput> getAllJobs() {
    return mapper.toDto(repository.findAll());
  }

  public List<JobOutput> getJobsByIdentifier(String identifier) {
    return Stream.of(
            getJobByFunction(
                repository::findById, NumberUtil.parseDatabaseIdIfPossible(identifier)),
            getJobByFunction(repository::findByName, identifier))
        .filter(Optional::isPresent)
        .map(Optional::get)
        .toList();
  }

  private <T> Optional<JobOutput> getJobByFunction(Function<T, Optional<Job>> func, T param) {
    return getByFunction(func, param).map(mapper::toDto);
  }

  private <T> Optional<Job> getByFunction(Function<T, Optional<Job>> func, T param) {
    return func.apply(param);
  }

  public Optional<JobOutput> getJobById(Integer id) {
    return getJobByFunction(repository::findById, id);
  }

  public Optional<JobOutput> getJobByName(String name) {
    return getJobByFunction(repository::findByName, name);
  }
}
