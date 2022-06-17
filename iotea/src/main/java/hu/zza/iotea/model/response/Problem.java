package hu.zza.iotea.model.response;

import java.net.URI;
import lombok.Getter;
import org.zalando.problem.*;

@Getter
public abstract class Problem extends AbstractThrowableProblem implements Response {
  private final Long deviceId;
  private final Long commandId;

  protected Problem(
      Long deviceId, Long commandId, URI type, String title, StatusType status, String detail) {
    super(type, title, status, detail);
    this.deviceId = deviceId;
    this.commandId = commandId;
  }

  protected Problem(
      Long deviceId, Long commandId, String type, String title, int statusCode, String detail) {
    this(deviceId, commandId, URI.create(type), title, Status.valueOf(statusCode), detail);
  }

  @Override
  public String getUri() {
    return getType().toString();
  }

  @Override
  public int getStatusCode() {
    return getStatus().getStatusCode();
  }
}
