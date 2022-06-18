package hu.zza.iotea.model;

import java.util.Objects;
import java.util.Set;
import javax.persistence.*;
import lombok.*;
import lombok.ToString.Exclude;
import org.hibernate.Hibernate;

@Entity
@Table(name = "jobs")
@Getter
@Setter
@ToString
@RequiredArgsConstructor
public class Job implements Identifiable {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(nullable = false)
  private Integer id;

  @Column(nullable = false, unique = true)
  private String name;

  @Exclude
  @ManyToMany
  @JoinTable(
      name = "job_device",
      joinColumns = @JoinColumn(name = "job_id"),
      inverseJoinColumns = @JoinColumn(name = "device_id"))
  private Set<Device> devices;

  @Exclude
  @ManyToMany
  @JoinTable(
      name = "job_command",
      joinColumns = @JoinColumn(name = "job_id"),
      inverseJoinColumns = @JoinColumn(name = "command_id"))
  private Set<Command> commands;

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
