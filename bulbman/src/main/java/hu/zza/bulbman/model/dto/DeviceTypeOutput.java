package hu.zza.bulbman.model.dto;

import lombok.Data;

@Data
public class DeviceTypeOutput {
  private Long id;
  private String brand;
  private String model;
  private String version;
}
