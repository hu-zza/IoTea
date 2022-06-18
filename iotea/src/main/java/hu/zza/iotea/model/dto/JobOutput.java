package hu.zza.iotea.model.dto;

import hu.zza.iotea.model.Run;
import lombok.Data;

@Data
public class JobOutput {
  private Integer id;
  private String name;
  private DeviceOutput device;
  private CommandOutput command;
  private Run lastRun;
}
