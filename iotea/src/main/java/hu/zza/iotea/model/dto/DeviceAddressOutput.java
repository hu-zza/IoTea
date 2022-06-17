package hu.zza.iotea.model.dto;

import java.net.InetAddress;
import lombok.Data;

@Data
public class DeviceAddressOutput {
  private InetAddress ip;
  private boolean reachable;
}
