package hu.zza.iotea.controller;

import hu.zza.iotea.model.dto.JobOutput;
import hu.zza.iotea.service.JobService;
import java.util.List;
import java.util.Optional;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api")
@AllArgsConstructor
public class EasyJobController {
  private JobService service;

  @GetMapping
  public List<JobOutput> getAllJobs() {
    return service.getAllJobs();
  }

  @GetMapping("/{identifier}")
  public List<JobOutput> getJobsByIdentifier(@PathVariable String identifier) {
    return service.getJobsByIdentifier(identifier);
  }

  @GetMapping("/id/{id}")
  public Optional<JobOutput> getJobById(@PathVariable Integer id) {
    return service.getJobById(id);
  }

  @GetMapping("/name/{name}")
  public Optional<JobOutput> getJobByName(@PathVariable String name) {
    return service.getJobByName(name);
  }

  @GetMapping("/{deviceName}/{commandName}")
  public JobOutput createJob(@PathVariable String deviceName, @PathVariable String commandName) {
    return service.createJob(deviceName, commandName);
  }

  @GetMapping("/{deviceName}/{commandName}/name/{jobName}")
  public JobOutput createNamedJob(
      @PathVariable String jobName,
      @PathVariable String deviceName,
      @PathVariable String commandName) {
    return service.createNamedJob(jobName, deviceName, commandName);
  }

  @GetMapping("/call/{id}/as/{name}")
  public JobOutput setJobName(@PathVariable Integer id, @PathVariable String name) {
    return service.setName(id, name);
  }

  @GetMapping("/run/{name}")
  public JobOutput runJobByNameAlias(@PathVariable String name) {
    return service.runJob(name);
  }

  @GetMapping("/run/{name}/{parameters}")
  public JobOutput runJobByNameAlias(@PathVariable String name, @PathVariable String parameters) {
    return service.runJob(name, parameters);
  }
}
