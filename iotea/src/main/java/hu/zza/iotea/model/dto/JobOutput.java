package hu.zza.iotea.model.dto;

import lombok.Data;

@Data
public class JobOutput {
  private Integer id;
  private String name;
  private DeviceOutput device;
  private CommandOutput command;
}
