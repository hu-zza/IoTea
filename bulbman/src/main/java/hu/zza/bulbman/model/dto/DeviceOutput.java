package hu.zza.bulbman.model.dto;

import lombok.Data;

@Data
public class DeviceOutput {
  private String id;
  private DeviceTypeOutput type;
  private DeviceAddressOutput address;
  private int port;
  private String name;
}

