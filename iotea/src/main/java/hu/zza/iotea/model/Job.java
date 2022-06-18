package hu.zza.iotea.model;

import hu.zza.iotea.model.Run.RunBuilder;
import hu.zza.iotea.model.util.ParameterUtil;
import hu.zza.iotea.service.Commander;
import java.time.LocalDateTime;
import java.util.Objects;
import javax.persistence.*;
import lombok.*;
import lombok.experimental.FieldNameConstants.Exclude;
import org.hibernate.Hibernate;

@Entity
@Table(name = "jobs")
@Getter
@Setter
@ToString
@NoArgsConstructor(access = AccessLevel.PACKAGE)
public class Job implements Identifiable {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(nullable = false)
  private Integer id;

  @Column(nullable = false, unique = true)
  private String name;

  @ManyToOne
  @JoinColumn(name = "device_id", nullable = false)
  private Device device;

  @ManyToOne
  @JoinColumn(name = "command_id", nullable = false)
  private Command command;

  @Transient private Run lastRun;

  @Transient private Run.RunBuilder runBuilder;

  public Job(Integer id, String name, Device device, Command command) {
    this.id = id;
    this.name = name;
    this.device = device;
    this.command = command;
  }

  public String run(Commander commander, String parameters) {
    runBuilder = new RunBuilder().started(LocalDateTime.now()).rawParameters(parameters);
    return run(commander, ParameterUtil.prepareParameters(parameters));
  }

  public String run(Commander commander, Object... parameters) {
    if (runBuilder == null) {
      runBuilder = new Run.RunBuilder().started(LocalDateTime.now());
    }
    var payload = command.build(parameters);
    var response = commander.sendPayload(device, payload);
    lastRun = runBuilder.parameters(parameters).payload(payload).response(response).build();
    runBuilder = null;
    return response;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || Hibernate.getClass(this) != Hibernate.getClass(o)) {
      return false;
    }
    Job job = (Job) o;
    return id != null && Objects.equals(id, job.id);
  }

  @Override
  public int hashCode() {
    return getClass().hashCode();
  }
}
