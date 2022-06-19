package hu.zza.iotea.model.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import javax.validation.constraints.*;
import lombok.Data;

@Data
public class JobInput {
  @NotBlank(message = "Field 'name' cannot be null or blank.")
  @Size(min = 1, max = 255, message = "The length of the field 'name' should be between 1 and 255.")
  private String name;

  @JsonProperty("device_id")
  @NotNull(message = "Field 'device_id' cannot be null.")
  @Min(value = 1, message = "The value of the field 'device_id' cannot be less than 1.")
  @Max(
      value = Integer.MAX_VALUE,
      message = "The value of the field 'id' cannot be more than 2147483647.")
  private Integer deviceId;

  @JsonProperty("command_id")
  @NotNull(message = "Field 'command_id' cannot be null.")
  @Min(value = 1, message = "The value of the field 'command_id' cannot be less than 1.")
  @Max(
      value = Integer.MAX_VALUE,
      message = "The value of the field 'id' cannot be more than 2147483647.")
  private Integer commandId;
}
