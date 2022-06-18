package hu.zza.iotea.model;

import hu.zza.iotea.model.util.ParameterUtil;
import hu.zza.iotea.service.Commander;
import java.util.Objects;
import javax.persistence.*;
import lombok.*;
import org.hibernate.Hibernate;

@Entity
@Table(name = "jobs")
@Getter
@Setter
@ToString
@AllArgsConstructor
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

  public String run(Commander commander, String parameters) {
    return run(commander, ParameterUtil.prepareParameters(parameters));
  }

  public String run(Commander commander, Object... parameters) {
    return commander.sendPayload(device, command.build(parameters));
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
