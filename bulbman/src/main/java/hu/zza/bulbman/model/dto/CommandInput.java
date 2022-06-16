package hu.zza.bulbman.model.dto;

import javax.validation.constraints.*;
import lombok.Data;

@Data
public class CommandInput {
  @NotBlank(message = "Field 'id' cannot be null or blank.")
  @Size(min = 1, max = 255, message = "The length of the field 'id' should be between 1 and 255.")
  private String id;

  @NotBlank(message = "Field 'template' cannot be null or blank.")
  private String template;

  private String note;
}
