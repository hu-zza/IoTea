package hu.zza.iotea.controller;

import hu.zza.iotea.model.dto.JobOutput;
import hu.zza.iotea.service.JobService;
import java.util.List;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api")
@AllArgsConstructor
public class EasyJobController {
  private JobController controller;
  private JobService service;

  @GetMapping
  public List<JobOutput> getAllJobs() {
    return controller.getAllJobs();
  }

  @GetMapping("/{identifier}")
  public List<JobOutput> getJobsByIdentifier(@PathVariable String identifier) {
    return controller.getJobsByIdentifier(identifier);
  }

  @GetMapping("/id/{id}")
  public JobOutput getJobById(@PathVariable Integer id) {
    return controller.getJobById(id);
  }

  @GetMapping("/name/{name}")
  public JobOutput getJobByName(@PathVariable String name) {
    return controller.getJobByName(name);
  }

  @GetMapping("/{commandName}/{deviceName}")
  @ResponseStatus(HttpStatus.CREATED)
  public JobOutput createJob(@PathVariable String commandName, @PathVariable String deviceName) {
    return service.createJob(commandName, deviceName);
  }

  @GetMapping("/{commandName}/{deviceName}/as/{jobName}")
  @ResponseStatus(HttpStatus.CREATED)
  public JobOutput createNamedJob(
      @PathVariable String commandName,
      @PathVariable String deviceName,
      @PathVariable String jobName) {
    return service.createNamedJob(commandName, deviceName, jobName);
  }

  @GetMapping("/call/{id}/as/{name}")
  public JobOutput setJobName(@PathVariable Integer id, @PathVariable String name) {
    return service.setName(id, name);
  }

  @GetMapping("/run/{name}")
  @ResponseStatus(HttpStatus.ACCEPTED)
  public JobOutput runJobByName(@PathVariable String name) {
    return service.runJob(name);
  }

  @GetMapping("/run/{name}/{parameters}")
  @ResponseStatus(HttpStatus.ACCEPTED)
  public JobOutput runJobByName(@PathVariable String name, @PathVariable String parameters) {
    return service.runJob(name, parameters);
  }

  @GetMapping("/delete/{name}")
  @ResponseStatus(HttpStatus.NO_CONTENT)
  public void deleteJobByName(@PathVariable String name) {
    controller.deleteJobByName(name);
  }
}
