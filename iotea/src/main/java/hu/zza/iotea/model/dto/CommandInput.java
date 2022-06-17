package hu.zza.iotea.model.dto;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import lombok.Data;

@Data
public class CommandInput {
  @NotBlank(message = "Field 'name' cannot be null or blank.")
  @Size(min = 1, max = 255, message = "The length of the field 'name' should be between 1 and 255.")
  private String name;

  @NotBlank(message = "Field 'template' cannot be null or blank.")
  private String template;

  private String note;
}
