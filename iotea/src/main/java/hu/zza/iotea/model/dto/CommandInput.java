package hu.zza.iotea.model.dto;

import javax.validation.constraints.*;
import lombok.Data;

@Data
public class CommandInput {
  @NotBlank(message = "Field 'name' cannot be null or blank.")
  @Size(min = 1, max = 255, message = "The length of the field 'name' should be between 1 and 255.")
  private String name;

  @NotNull(message = "Field 'template' cannot be null.")
  @Size(max = 30000, message = "The length of the field 'name' should be less than 30000.")
  private String template;

  @Size(max = 30000, message = "The length of the field 'note' should be less than 30000.")
  private String note;
}
