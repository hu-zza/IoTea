package hu.zza.iotea.model.dto;

import hu.zza.iotea.model.response.Response;
import lombok.Data;

@Data
public class SimpleResponse implements Response {
  private String uri;
  private String title;
  private Integer statusCode;
  private String detail;
}
