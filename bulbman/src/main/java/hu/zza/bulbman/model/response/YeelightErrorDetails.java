package hu.zza.bulbman.model.response;

import lombok.Data;

@Data
public class YeelightErrorDetails {
  private int code;
  private String message;
}
