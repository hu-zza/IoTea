package hu.zza.iotea.model.dto;

import lombok.Data;

@Data
public class CommandOutput {
  private Long id;
  private String name;
  private String template;
  private String note;
}
