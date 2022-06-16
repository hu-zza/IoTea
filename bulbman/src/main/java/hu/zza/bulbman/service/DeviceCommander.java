package hu.zza.bulbman.service;

import hu.zza.bulbman.model.Device;
import hu.zza.bulbman.model.response.CommanderProblem;
import hu.zza.bulbman.model.response.Response;
import hu.zza.bulbman.service.connection.Sender;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

@Service
public class DeviceCommander {
  private final Logger logger = LoggerFactory.getLogger(DeviceCommander.class);
  private Sender sender;

  @Autowired
  @Qualifier("telnetSender")
  public void setSender(Sender sender) {
    this.sender = sender;
  }

  public Response sendPayload(Device device, String payload) {
    try {
      String rawResponse = sender.send(device.getAddress(), device.getPort(), payload);
      return null;//objectMapper.readValue(rawResponse, Response.class);

    } catch (Exception exception) {
      var message =
          "Cannot send command to Addressable(address=%s, port=%d)"
              .formatted(device.getAddress(), device.getPort());
      logger.warn(message, exception);
      return new CommanderProblem(device.getId(), "TODO", message);
    }
  }
}
