package hu.zza.iotea.model;

import java.util.Objects;
import javax.persistence.*;
import lombok.*;
import org.hibernate.Hibernate;
import org.springframework.data.domain.Persistable;

@Entity
@Table(name = "devices")
@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PACKAGE)
public class Device implements Identifiable, Persistable<Integer>{
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(nullable = false)
  private Integer id;

  @Column(nullable = false, unique = true)
  private String uid;

  @Column(nullable = false, unique = true)
  private String name;

  @Embedded
  @Column(nullable = false)
  private DeviceAddress address;

  @Column(nullable = false)
  private Integer port;

  @Transient
  private boolean isNew = true;

  @PostLoad
  @PrePersist
  void trackNotNew() {
    this.isNew = false;
  }

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
