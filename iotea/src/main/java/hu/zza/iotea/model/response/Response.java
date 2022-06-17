package hu.zza.iotea.model.response;

import com.fasterxml.jackson.annotation.JsonProperty;

public interface Response {
  Long getDeviceId();

  Long getCommandId();

  @JsonProperty("type")
  String getUri();

  String getTitle();

  @JsonProperty("status")
  int getStatusCode();

  String getDetail();
}
