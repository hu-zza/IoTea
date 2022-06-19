package hu.zza.iotea.model;

import javax.validation.constraints.NotNull;
import lombok.Data;

@Data
public class JobContext {
  @NotNull(message = "Field 'parameters' cannot be null.")
  private Object[] parameters;
}
