package hu.zza.iotea.model.response;

import com.fasterxml.jackson.annotation.JsonProperty;

public interface Response {
  @JsonProperty("type")
  String getUri();

  String getTitle();

  @JsonProperty("status")
  Integer getStatusCode();

  String getDetail();
}
