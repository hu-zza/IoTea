package hu.zza.iotea.service;

import hu.zza.iotea.model.Job;
import hu.zza.iotea.model.dto.JobOutput;
import hu.zza.iotea.model.response.ServiceProblem;
import hu.zza.iotea.model.util.JobOutputMapper;
import hu.zza.iotea.model.util.NumberUtil;
import hu.zza.iotea.repository.JobRepository;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Stream;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@AllArgsConstructor
public class JobService {
  private JobRepository repository;
  private JobOutputMapper mapper;

  private DeviceCommander commander;

  private DeviceService deviceService;
  private CommandService commandService;

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

  public JobOutput createJob(String deviceName, String commandName) {
    return createNamedJob(UUID.randomUUID().toString(), deviceName, commandName);
  }

  public JobOutput createNamedJob(String jobName, String deviceName, String commandName) {
    var optJob = createOptionalJob(jobName, deviceName, commandName);
    return optJob
        .map(repository::save)
        .map(mapper::toDto)
        .orElseThrow(() -> new ServiceProblem("")); // TODO
  }

  private Optional<Job> createOptionalJob(String jobName, String deviceName, String commandName) {
    var device = deviceService.getByName(deviceName);
    var command = commandService.getByName(commandName);

    return Optional.ofNullable(
        command.isPresent() && device.isPresent()
            ? new Job(null, jobName, device.get(), command.get())
            : null);
  }

  public JobOutput setName(Integer id, String name) {
    var job = getById(id);
    job.setName(name);
    return mapper.toDto(job);
  }

  private Job getById(Integer id) {
    return repository.findById(id).orElseThrow(() -> new ServiceProblem("")); // TODO
  }

  public JobOutput runJob(String name) {
    return runJob(name, "");
  }

  public JobOutput runJob(Integer id) {
    return runJob(id, "");
  }

  private Job getByName(String name) {
    return repository.findByName(name).orElseThrow(() -> new ServiceProblem("")); // TODO
  }

  public JobOutput runJob(String name, String parameters) {
    return runJob(getByName(name), parameters);
  }

  private JobOutput runJob(Job job, String parameters) {
    job.run(commander, parameters); // TODO
    return mapper.toDto(job);
  }

  public JobOutput runJob(Integer id, String parameters) {
    return runJob(getById(id), parameters);
  }

  public void deleteById(Integer id) {
    repository.deleteById(id);
  }

  @Transactional
  public void deleteByName(String name) {
    repository.deleteByName(name);
  }
}
