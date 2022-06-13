package hu.zza.bulbman.model.dto;

import lombok.Data;

@Data
public class DeviceOutput {
  private String id;
  private String name;
  private DeviceAddressOutput address;
}

