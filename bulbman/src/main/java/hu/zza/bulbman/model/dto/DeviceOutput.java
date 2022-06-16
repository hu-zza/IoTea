package hu.zza.bulbman.model.dto;

import lombok.Data;

@Data
public class DeviceOutput {
  private String id;
  private DeviceAddressOutput ip;
  private int port;
  private String name;
}
