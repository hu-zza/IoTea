package hu.zza.iotea.controller;

import hu.zza.iotea.model.dto.JobOutput;
import hu.zza.iotea.model.response.Response;
import hu.zza.iotea.service.JobService;
import java.util.List;
import java.util.Optional;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api")
@AllArgsConstructor
public class JobController {
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
    return null;
  }

  @GetMapping("/{deviceName}/{commandName}/{parameters}")
  public JobOutput createJob(
      @PathVariable String deviceName,
      @PathVariable String commandName,
      @PathVariable String parameters) {
    return null;
  }

  @GetMapping("/id/{id}/{parameters}")
  public Response runJobById(@PathVariable Integer id, @PathVariable String parameters) {
    return null;
  }

  @GetMapping("/name/{name}/{parameters}")
  public Response runJobByName(@PathVariable String name, @PathVariable String parameters) {
    return null;
  }
}
