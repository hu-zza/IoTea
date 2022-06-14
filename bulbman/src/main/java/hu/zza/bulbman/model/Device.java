package hu.zza.bulbman.model;

import java.util.Objects;
import javax.persistence.*;
import lombok.*;
import org.hibernate.Hibernate;

@Entity
@Table(name = "devices")
@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class Device implements Addressable {
  @Id
  @Column(nullable = false)
  private String id;

  @Column(nullable = false)
  private String name;

  @Embedded
  @Column(nullable = false)
  private DeviceAddress address;

  @Column(nullable = false)
  private int port;

  @Enumerated(EnumType.STRING)
  private DeviceType type;

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || Hibernate.getClass(this) != Hibernate.getClass(o)) {
      return false;
    }
    Device other = (Device) o;
    return getId() != null && Objects.equals(getId(), other.getId());
  }

  @Override
  public int hashCode() {
    return getClass().hashCode();
  }
}

