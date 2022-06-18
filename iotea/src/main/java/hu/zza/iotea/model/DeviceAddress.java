package hu.zza.iotea.model;

import hu.zza.iotea.model.util.InetAddressConverter;
import java.io.IOException;
import java.net.InetAddress;
import javax.persistence.*;
import lombok.*;

@Embeddable
@Data
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class DeviceAddress {
  public static final InetAddress NULL_IP = InetAddress.getLoopbackAddress();
  private static final InetAddressConverter converter = new InetAddressConverter();

  private static final int REACHABLE_TIMEOUT_MS = 1000;

  @Convert(converter = InetAddressConverter.class)
  private InetAddress ip;

  public DeviceAddress(InetAddress ip) {
    this.ip = ip;
  }

  public DeviceAddress(String ip) {
    this(converter.parse(ip));
  }

  public boolean isReachable() {

    if (ip.isLoopbackAddress()) {
      return false;
    } else {
      return isReachableByPing();
    }
  }

  private boolean isReachableByPing() {
    try {
      return ip.isReachable(REACHABLE_TIMEOUT_MS);
    } catch (IOException ignored) {
      return false;
    }
  }

  @Transient
  public String getAsDatabaseColumn() {
    return converter.convertToDatabaseColumn(ip);
  }
}
