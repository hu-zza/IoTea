package hu.zza.iotea.model.response;

import static java.net.HttpURLConnection.HTTP_INTERNAL_ERROR;

public class CommanderProblem extends Problem {
  public CommanderProblem(String detail) {
    super("IoTea/commander-exception", "Commander exception", HTTP_INTERNAL_ERROR, detail);
  }
}
