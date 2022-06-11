package hu.zza.bulbman.model.general;

import hu.zza.bulbman.model.DeviceAddress;
import hu.zza.bulbman.model.util.InetAddressConverter;
import java.net.InetAddress;
import java.util.Objects;
import javax.persistence.*;
import lombok.*;
import org.hibernate.Hibernate;

@Entity
@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class GeneralDeviceAddress implements DeviceAddress {
  @Id
  @Column(name = "id", nullable = false)
  private Long id;

  @Convert(converter = InetAddressConverter.class)
  private InetAddress ip;

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || Hibernate.getClass(this) != Hibernate.getClass(o)) {
      return false;
    }
    GeneralDeviceAddress that = (GeneralDeviceAddress) o;
    return id != null && Objects.equals(id, that.id);
  }

  @Override
  public int hashCode() {
    return getClass().hashCode();
  }
}
