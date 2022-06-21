package hu.zza.iotea.model.exception;

import static java.net.HttpURLConnection.HTTP_NOT_FOUND;

public class EntityNotFoundProblem extends Problem {
  public EntityNotFoundProblem(String detail) {
    super("IoTea/entity-not-found-exception", "Entity not found exception", HTTP_NOT_FOUND, detail);
  }
}
