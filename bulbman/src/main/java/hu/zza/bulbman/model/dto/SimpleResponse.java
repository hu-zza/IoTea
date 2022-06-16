package hu.zza.bulbman.model.dto;

import hu.zza.bulbman.model.response.Response;
import lombok.Data;

@Data
public class SimpleResponse implements Response {
  private String deviceId;
  private String commandId;
  private String uri;
  private String title;
  private int statusCode;
  private String detail;
}
