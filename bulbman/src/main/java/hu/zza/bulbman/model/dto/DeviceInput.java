package hu.zza.bulbman.model.dto;

import javax.validation.constraints.*;
import lombok.Data;

@Data
public class DeviceInput {
  @NotBlank(message = "Field 'id' cannot be null or blank.")
  @Size(min = 1, max = 255, message = "The length of the field 'id' should be between 1 and 255.")
  private String id;

  @NotBlank(message = "Field 'name' cannot be null or blank.")
  @Size(min = 1, max = 255, message = "The length of the field 'name' should be between 1 and 255.")
  private String name;

  @NotBlank(message = "Field 'ip' cannot be null or blank.")
  @Size(min = 1, max = 255, message = "The length of the field 'ip' should be between 1 and 255.")
  private String ip;

  @NotNull(message = "Field 'port' cannot be null.")
  @Size(min = 0, max = 65535, message = "The value of the field 'port' should be between 0 and 65535.")
  private Integer port;

  @NotBlank(message = "Field 'type' cannot be null or blank.")
  @Size(min = 1, max = 255, message = "The length of the field 'type' should be between 1 and 255.")
  private String type;
}
