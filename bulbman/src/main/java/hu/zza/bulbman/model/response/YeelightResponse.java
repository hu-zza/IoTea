package hu.zza.bulbman.model.response;

import java.util.List;
import lombok.Data;

@Data
public class YeelightResponse implements Response {
  private int id;
  private List<String> result;
  private YeelightErrorDetails error;
}
