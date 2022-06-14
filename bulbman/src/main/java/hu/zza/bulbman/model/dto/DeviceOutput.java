package hu.zza.bulbman.model.dto;

import hu.zza.bulbman.model.DeviceType;
import lombok.Data;

@Data
public class DeviceOutput {
  private String id;
  private String name;
  private DeviceAddressOutput address;
  private int port;
  private DeviceType type;
}

