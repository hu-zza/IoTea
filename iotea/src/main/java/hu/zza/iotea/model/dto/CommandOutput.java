package hu.zza.iotea.model.dto;

import hu.zza.iotea.model.Identifiable;
import lombok.Data;

@Data
public class CommandOutput implements Identifiable {
  private Integer id;
  private String name;
  private String template;
  private String note;
}
