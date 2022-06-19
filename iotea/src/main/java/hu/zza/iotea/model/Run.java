package hu.zza.iotea.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.time.LocalDateTime;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class Run {
  private LocalDateTime started;

  @JsonProperty("raw_parameters")
  private String rawParameters;

  private Object[] parameters;
  private String payload;
  private String response;
}
