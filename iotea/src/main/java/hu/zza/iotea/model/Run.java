package hu.zza.iotea.model;

import java.time.LocalDateTime;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class Run {
  private LocalDateTime started;
  private String rawParameters;
  private Object[] parameters;
  private String payload;
  private String response;
}
