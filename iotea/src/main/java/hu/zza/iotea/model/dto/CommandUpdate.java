package hu.zza.iotea.model.dto;

import javax.validation.constraints.*;
import lombok.Data;

@Data
public class CommandUpdate {
  @NotNull(message = "Field 'id' cannot be null.")
  @Min(value = 1, message = "The value of the field 'id' cannot be less than 1.")
  @Max(
      value = Integer.MAX_VALUE,
      message = "The value of the field 'id' cannot be more than 2147483647.")
  private Integer id;

  @NotBlank(message = "Field 'name' cannot be null or blank.")
  @Size(min = 1, max = 255, message = "The length of the field 'name' should be between 1 and 255.")
  private String name;

  @NotBlank(message = "Field 'template' cannot be null or blank.")
  private String template;

  private String note;
}
