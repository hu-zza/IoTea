package hu.zza.bulbman.model.response;

import com.fasterxml.jackson.annotation.JsonProperty;

public interface Response {
  String getDeviceId();

  String getCommandId();

  @JsonProperty("type")
  String getUri();

  String getTitle();

  @JsonProperty("status")
  int getStatusCode();

  String getDetail();
}
