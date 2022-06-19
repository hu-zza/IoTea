package hu.zza.iotea.model.dto;

import lombok.Data;

@Data
public class DeviceAddressOutput {
  private String ip;
  private Boolean reachable;
}
