package hu.zza.bulbman.model.dto;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

public class DeviceTypeInput {
  @NotBlank(message = "Field 'brand' cannot be null or blank.")
  @Size(min = 1, max = 255, message = "The length of the field 'brand' should be between 1 and 255.")
  private String brand;

  @NotBlank(message = "Field 'model' cannot be null or blank.")
  @Size(min = 1, max = 255, message = "The length of the field 'model' should be between 1 and 255.")
  private String model;

  @NotBlank(message = "Field 'version' cannot be null or blank.")
  @Size(min = 1, max = 255, message = "The length of the field 'version' should be between 1 and 255.")
  private String version;
}
