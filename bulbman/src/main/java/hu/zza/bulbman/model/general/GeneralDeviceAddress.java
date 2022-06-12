package hu.zza.bulbman.model.general;

import hu.zza.bulbman.model.DeviceAddress;
import hu.zza.bulbman.model.util.InetAddressConverter;
import java.io.IOException;
import java.net.InetAddress;
import javax.persistence.*;
import lombok.*;

@Embeddable
@Data
@NoArgsConstructor
public class GeneralDeviceAddress implements DeviceAddress {
  private static final int REACHABLE_TIMEOUT_MS = 1000;
  @Convert(converter = InetAddressConverter.class)
  private InetAddress ip;

  @Transient
  private boolean reachable;

  public GeneralDeviceAddress(InetAddress ip) {
    this.ip = ip;
    updateReachable();
  }

  public void updateReachable() {
    if (ip.isLoopbackAddress()) {
      reachable = false;
    } else {
      pingAndSetReachable();
    }
  }

  private void pingAndSetReachable() {
    try {
      reachable = ip.isReachable(REACHABLE_TIMEOUT_MS);
    } catch (IOException ignored) {
      reachable = false;
    }
  }
}
