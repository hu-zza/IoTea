package hu.zza.iotea.model.dto;

import lombok.Data;

@Data
public class DeviceOutput {
  private Long id;
  private String name;
  private DeviceAddressOutput ip;
  private int port;
}
