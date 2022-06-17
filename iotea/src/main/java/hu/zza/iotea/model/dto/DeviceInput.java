package hu.zza.iotea.model.dto;

import javax.validation.constraints.*;
import lombok.Data;

@Data
public class DeviceInput {
  @NotBlank(message = "Field 'name' cannot be null or blank.")
  @Size(min = 1, max = 255, message = "The length of the field 'name' should be between 1 and 255.")
  private String name;

  @NotBlank(message = "Field 'ip' cannot be null or blank.")
  @Size(min = 1, max = 255, message = "The length of the field 'ip' should be between 1 and 255.")
  private String ip;

  @NotNull(message = "Field 'port' cannot be null.")
  @Min(value = 0, message = "The value of the field 'port' cannot be less than 0.")
  @Max(value = 65535, message = "The value of the field 'port' cannot be more than 65535.")
  private Integer port;
}
