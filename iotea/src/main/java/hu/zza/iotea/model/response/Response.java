package hu.zza.iotea.model.response;

import com.fasterxml.jackson.annotation.JsonProperty;

public interface Response {
  Integer getDeviceId();

  Integer getCommandId();

  @JsonProperty("type")
  String getUri();

  String getTitle();

  @JsonProperty("status")
  int getStatusCode();

  String getDetail();
}
