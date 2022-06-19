package hu.zza.iotea.model.exception;

import static java.net.HttpURLConnection.HTTP_INTERNAL_ERROR;

public class ConnectionProblem extends Problem {
  public ConnectionProblem(String detail) {
    super("IoTea/connection-exception", "Connection exception", HTTP_INTERNAL_ERROR, detail);
  }
}
