package hu.zza.iotea.model.exception;

import static java.net.HttpURLConnection.HTTP_BAD_REQUEST;

public class ServiceProblem extends Problem {
  public ServiceProblem(String detail) {
    super("IoTea/service-exception", "Service exception", HTTP_BAD_REQUEST, detail);
  }
}
