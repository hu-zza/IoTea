package hu.zza.iotea.model;

import hu.zza.iotea.model.util.Commands;
import java.util.Objects;
import java.util.regex.Pattern;
import javax.persistence.*;
import lombok.*;
import org.hibernate.Hibernate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Entity
@Table(name = "commands")
@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PACKAGE)
public class Command implements Identifiable {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(nullable = false)
  private Integer id;

  @Column(nullable = false, unique = true)
  private String name;

  @Column(nullable = false)
  private String template;

  @Column(nullable = false)
  private String note = "";

  public void setNote(String note) {
    this.note = note != null ? note : "";
  }

  public String build(Object... args) {
    return Commands.build(template, args);
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
