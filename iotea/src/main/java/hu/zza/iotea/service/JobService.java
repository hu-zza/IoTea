package hu.zza.iotea.service;

import hu.zza.iotea.model.Job;
import hu.zza.iotea.model.JobContext;
import hu.zza.iotea.model.dto.JobInput;
import hu.zza.iotea.model.dto.JobOutput;
import hu.zza.iotea.model.exception.EntityNotFoundProblem;
import hu.zza.iotea.model.exception.ServiceProblem;
import hu.zza.iotea.model.util.Numbers;
import hu.zza.iotea.model.util.Parameters;
import hu.zza.iotea.model.util.mapping.JobInputMapper;
import hu.zza.iotea.model.util.mapping.JobOutputMapper;
import hu.zza.iotea.repository.JobRepository;
import hu.zza.iotea.service.connection.Commander;
import java.net.URI;
import java.util.*;
import java.util.function.Function;
import java.util.function.Supplier;
import java.util.stream.Stream;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@AllArgsConstructor
public class JobService {
  private JobRepository repository;
  private JobInputMapper inMapper;
  private JobOutputMapper outMapper;

  private Commander commander;

  private DeviceService deviceService;
  private CommandService commandService;

  public Optional<Integer> getIdByName(String name) {
    return repository.getIdByName(name);
  }

  public List<JobOutput> getAllJobs() {
    return outMapper.toDto(repository.findAll());
  }

  public List<JobOutput> getJobsByIdentifier(String identifier) {
    return Stream.of(
            getJobByFunction(repository::findById, Numbers.parseDatabaseIdIfPossible(identifier)),
            getJobByFunction(repository::findByName, identifier))
        .filter(Optional::isPresent)
        .map(Optional::get)
        .toList();
  }

  private <T> Optional<JobOutput> getJobByFunction(Function<T, Optional<Job>> func, T param) {
    return getByFunction(func, param).map(outMapper::toDto);
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

  public JobOutput saveJob(JobInput jobInput) {
    return saveJob(inMapper.toEntity(jobInput));
  }

  private JobOutput saveJob(Job job) {
    return outMapper.toDto(repository.save(job));
  }

  @Transactional
  public ResponseEntity<JobOutput> updateJob(
      Supplier<Optional<Integer>> idSupplier, JobInput input) {
    var optId = idSupplier.get();
    var job = inMapper.toEntity(input);
    optId.ifPresentOrElse(job::setId, () -> job.setId(null));

    if (optId.isPresent()) {
      var id = optId.get();

      if (!repository.existsById(id)) {
        repository.insertWithId(job);
        return ResponseEntity.created(URI.create("/api/jobs/id/%d".formatted(id)))
            .body(
                getJobById(id)
                    .orElseThrow(() -> new ServiceProblem("Cannot insert %s".formatted(job))));
      }
      return ResponseEntity.ok(saveJob(job));
    }
    var persisted = saveJob(job);
    var uri = URI.create("/api/jobs/id/%d".formatted(persisted.getId()));
    return ResponseEntity.created(uri).body(persisted);
  }

  public JobOutput setName(Integer id, String name) {
    var job = getById(id);
    job.setName(name);
    return outMapper.toDto(repository.save(job));
  }

  private Job getById(Integer id) {
    return repository
        .findById(id)
        .orElseThrow(() -> new EntityNotFoundProblem("There is no Job with id: %d".formatted(id)));
  }

  /**
   * Tries to find a short, relevant and unique name for job. If deviceName = "Desk" and commandName
   * = "Toggle"
   *
   * <p>First try: ToggleDesk, and then: ToggleDeskA, ToggleDeskB, ... ToggleDeskZ, finally it uses
   * a UUID.
   */
  public JobOutput createJob(String commandName, String deviceName) {
    String name = "%s%s".formatted(commandName, deviceName);

    JobOutput result = createJobIfNameAvailable(name, deviceName, commandName);

    String jobNameTemplate = "%s%s%%c".formatted(commandName, deviceName);

    for (int i = 65; i < 91 && result == null; i++) {
      result = createJobIfNameAvailable(jobNameTemplate.formatted(i), deviceName, commandName);
    }

    if (result != null) {
      return result;
    }
    return createNamedJob(commandName, deviceName, UUID.randomUUID().toString());
  }

  private JobOutput createJobIfNameAvailable(String name, String deviceName, String commandName) {
    if (repository.findByName(name).isEmpty()) {
      return createNamedJob(commandName, deviceName, name);
    }
    return null;
  }

  public JobOutput createNamedJob(String commandName, String deviceName, String jobName) {
    var job = createJob(jobName, deviceName, commandName);
    repository.save(job);
    return outMapper.toDto(job);
  }

  private Job createJob(String jobName, String deviceName, String commandName) {
    var device = deviceService.getByName(deviceName);
    var command = commandService.getByName(commandName);

    if (command.isPresent() && device.isPresent()) {
      return new Job(null, jobName, device.get(), command.get());
    } else {
      throw new ServiceProblem(
          "Cannot create Job (name: %s) from %s and %s.".formatted(jobName, device, command));
    }
  }

  private Job getByName(String name) {
    return repository
        .findByName(name)
        .orElseThrow(
            () -> new EntityNotFoundProblem("There is no Job with name: %s".formatted(name)));
  }

  public JobOutput runJob(String name) {
    return runJob(name, new JobContext());
  }

  public JobOutput runJob(String name, JobContext context) {
    return runJob(getByName(name), context);
  }

  private JobOutput runJob(Job job, JobContext context) {
    job.run(commander, context);
    return outMapper.toDto(job);
  }

  public JobOutput runJob(Integer id, JobContext context) {
    return runJob(getById(id), context);
  }

  public JobOutput runJob(String name, String rawParameters) {
    var context = new JobContext();
    context.setParameters(Parameters.prepareParameters(rawParameters));

    return runJob(name, context);
  }

  public void deleteById(Integer id) {
    repository.deleteById(id);
  }

  @Transactional
  public void deleteByName(String name) {
    repository.deleteByName(name);
  }
}
