package hu.zza.iotea.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.time.LocalDateTime;
import lombok.*;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Run {
  private LocalDateTime started;

  @JsonProperty("raw_parameters")
  private String rawParameters;

  private Object[] parameters;
  private String payload;
  private String response;
}
