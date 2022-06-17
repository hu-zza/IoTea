package hu.zza.iotea.model.dto;

import lombok.Data;

@Data
public class DeviceOutput {
  private Integer id;
  private String uid;
  private String name;
  private DeviceAddressOutput ip;
  private int port;
}
