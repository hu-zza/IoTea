package hu.zza.iotea.controller;

import hu.zza.iotea.model.JobContext;
import hu.zza.iotea.model.dto.JobInput;
import hu.zza.iotea.model.dto.JobOutput;
import hu.zza.iotea.model.exception.EntityNotFoundProblem;
import hu.zza.iotea.service.JobService;
import java.util.List;
import java.util.Optional;
import javax.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/jobs")
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
  public JobOutput getJobById(@PathVariable Integer id) {
    return service
        .getJobById(id)
        .orElseThrow(() -> new EntityNotFoundProblem("There is no Job with id: %d".formatted(id)));
  }

  @GetMapping("/name/{name}")
  public JobOutput getJobByName(@PathVariable String name) {
    return service
        .getJobByName(name)
        .orElseThrow(
            () -> new EntityNotFoundProblem("There is no Job with name: %s".formatted(name)));
  }

  @PostMapping
  @ResponseStatus(HttpStatus.CREATED)
  public JobOutput postJob(@Valid @RequestBody JobInput job) {
    return service.saveJob(job);
  }

  @PostMapping("/id/{id}")
  @ResponseStatus(HttpStatus.ACCEPTED)
  public JobOutput runJobById(@PathVariable Integer id, @Valid @RequestBody JobContext context) {
    return service.runJob(id, context);
  }

  @PostMapping("/name/{name}")
  @ResponseStatus(HttpStatus.ACCEPTED)
  public JobOutput runJobByName(@PathVariable String name, @Valid @RequestBody JobContext context) {
    return service.runJob(name, context);
  }

  @PutMapping("/id/{id}")
  public JobOutput putJobToId(@PathVariable Integer id, @Valid @RequestBody JobInput job) {
    return service.updateJob(() -> Optional.of(id), job);
  }

  @PutMapping("/name/{name}")
  public JobOutput putJobToName(@PathVariable String name, @Valid @RequestBody JobInput job) {
    job.setName(name);
    return service.updateJob(() -> service.getIdByName(name), job);
  }

  @DeleteMapping("/id/{id}")
  @ResponseStatus(HttpStatus.NO_CONTENT)
  public void deleteJobById(@PathVariable Integer id) {
    service.deleteById(id);
  }

  @DeleteMapping("/name/{name}")
  @ResponseStatus(HttpStatus.NO_CONTENT)
  public void deleteJobByName(@PathVariable String name) {
    service.deleteByName(name);
  }
}
