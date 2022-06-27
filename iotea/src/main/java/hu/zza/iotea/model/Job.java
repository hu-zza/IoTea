package hu.zza.iotea.model;

import hu.zza.iotea.service.connection.Commander;
import java.time.LocalDateTime;
import java.util.Objects;
import javax.persistence.*;
import lombok.*;
import org.hibernate.Hibernate;

@Entity
@Table(name = "jobs")
@Getter
@Setter
@NoArgsConstructor(access = AccessLevel.PACKAGE)
public class Job implements Identifiable {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Integer id;

  @Column(nullable = false, unique = true)
  private String name;

  @ManyToOne
  @JoinColumn(name = "device_id", nullable = false)
  private Device device;

  @ManyToOne
  @JoinColumn(name = "command_id", nullable = false)
  private Command command;

  @Transient private Run result;

  public Job(Integer id, String name, Device device, Command command) {
    this.id = id;
    this.name = name;
    this.device = device;
    this.command = command;
  }

  public String run(Commander commander, JobContext context) {
    var runBuilder =
        new Run.RunBuilder().started(LocalDateTime.now()).parameters(context.getParameters());

    var payload = command.build(context.getParameters());
    var response = commander.sendPayload(device, payload);

    result = runBuilder.payload(payload).response(response).build();
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

  @Override
  public String toString() {
    return "Job (id: %d, name: %s, device: %s, command: %s)".formatted(id, name, device, command);
  }
}
