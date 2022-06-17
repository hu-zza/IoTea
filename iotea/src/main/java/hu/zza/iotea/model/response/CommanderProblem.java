package hu.zza.iotea.model.response;

import static java.net.HttpURLConnection.HTTP_INTERNAL_ERROR;

public class CommanderProblem extends Problem {
  public CommanderProblem(Long deviceId, Long commandId, String detail) {
    super(
        deviceId,
        commandId,
        "IoTea/commander-exception",
        "Commander exception",
        HTTP_INTERNAL_ERROR,
        detail);
  }
}
