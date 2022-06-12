package hu.zza.bulbman.model.xiaomi.yeelight;

import hu.zza.bulbman.model.Device;
import hu.zza.bulbman.model.DeviceAddress;
import java.util.Objects;
import javax.persistence.*;
import lombok.*;
import org.hibernate.Hibernate;

@Entity
@Table(name = "xiaomi_yeelight_yeelight")
@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class YeelightDevice implements Device {
  @Id
  @Column(nullable = false)
  private String id;

  @Column(nullable = false)
  private String name;

  @Embedded
  @Column(nullable = false)
  private DeviceAddress address;

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || Hibernate.getClass(this) != Hibernate.getClass(o)) {
      return false;
    }
    YeelightDevice that = (YeelightDevice) o;
    return id != null && Objects.equals(id, that.id);
  }

  @Override
  public int hashCode() {
    return getClass().hashCode();
  }
}
