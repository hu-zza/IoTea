package hu.zza.bulbman.model.dto;

import java.net.InetAddress;
import lombok.Data;

@Data
public class DeviceAddressOutput {
  private InetAddress ip;
  private boolean reachable;
}
