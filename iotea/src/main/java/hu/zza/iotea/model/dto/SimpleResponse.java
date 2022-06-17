package hu.zza.iotea.model.dto;

import hu.zza.iotea.model.response.Response;
import lombok.Data;

@Data
public class SimpleResponse implements Response {
  private Long deviceId;
  private Long commandId;
  private String uri;
  private String title;
  private int statusCode;
  private String detail;
}
