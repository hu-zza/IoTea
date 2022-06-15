package hu.zza.bulbman.model.response;

import static java.net.HttpURLConnection.HTTP_BAD_REQUEST;

public class ServiceProblem extends Problem {
  public ServiceProblem(String detail) {
    super("bulbman/service-exception", "Service exception", HTTP_BAD_REQUEST, detail);
  }
}
