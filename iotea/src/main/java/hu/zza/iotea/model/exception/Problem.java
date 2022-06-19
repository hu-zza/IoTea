package hu.zza.iotea.model.exception;

import java.net.URI;
import lombok.Getter;
import org.zalando.problem.*;

@Getter
public abstract class Problem extends AbstractThrowableProblem {
  protected Problem(URI type, String title, StatusType status, String detail) {
    super(type, title, status, detail);
  }

  protected Problem(String type, String title, Integer statusCode, String detail) {
    this(URI.create(type), title, Status.valueOf(statusCode), detail);
  }
}
