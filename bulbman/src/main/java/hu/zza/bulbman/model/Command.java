package hu.zza.bulbman.model;

import java.util.Objects;
import javax.persistence.*;
import lombok.*;
import org.hibernate.Hibernate;

@Entity
@Table(name = "commands")
@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class Command {
  @Id
  @Column(nullable = false)
  private String id;

  @Column(nullable = false)
  private String template;

  @Column(nullable = false)
  private String note;

  public String build(Object... args) {
    return template.formatted(args);
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || Hibernate.getClass(this) != Hibernate.getClass(o)) {
      return false;
    }
    Command command = (Command) o;
    return id != null && Objects.equals(id, command.id);
  }

  @Override
  public int hashCode() {
    return getClass().hashCode();
  }
}
