package hu.zza.iotea.model;

import javax.persistence.Entity;
import javax.persistence.Table;
import lombok.NoArgsConstructor;
import org.springframework.data.domain.Persistable;
@Entity
@NoArgsConstructor
public class PersistableDevice extends Device implements Persistable<Integer> {

  @Override
  public boolean isNew() {
    return true;
  }

  public PersistableDevice(
      Integer id, String uid, String name, DeviceAddress address, Integer port) {
    super(id, uid, name, address, port, false);
  }

  public static PersistableDevice of(Device device) {
    return new PersistableDevice(
        device.getId(), device.getUid(), device.getName(), device.getAddress(), device.getPort());
  }
}
