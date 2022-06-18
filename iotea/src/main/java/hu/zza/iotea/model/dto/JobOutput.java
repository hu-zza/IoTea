package hu.zza.iotea.model.dto;

import java.util.List;
import lombok.Data;

@Data
public class JobOutput {
  private Integer id;
  private String name;
  private List<DeviceOutput> devices;
  private List<CommandOutput> commands;
}
