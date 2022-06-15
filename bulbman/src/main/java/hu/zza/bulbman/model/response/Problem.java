package hu.zza.bulbman.model.response;

import java.net.URI;
import org.zalando.problem.*;

public abstract class Problem extends AbstractThrowableProblem implements Response {
  protected Problem(URI type, String title, StatusType status, String detail) {
    super(type, title, status, detail);
  }

  protected Problem(String type, String title, int statusCode, String detail) {
    super(URI.create(type), title, Status.valueOf(statusCode), detail);
  }
}
