package hu.zza.bulbman.model.response;

import static java.net.HttpURLConnection.HTTP_INTERNAL_ERROR;

public class CommanderProblem extends Problem {
  public CommanderProblem(String deviceId, String commandId, String detail) {
    super(
        deviceId,
        commandId,
        "bulbman/commander-exception",
        "Commander exception",
        HTTP_INTERNAL_ERROR,
        detail);
  }
}
