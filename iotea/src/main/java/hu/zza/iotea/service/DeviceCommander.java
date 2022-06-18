package hu.zza.iotea.service;

import hu.zza.iotea.model.Device;
import hu.zza.iotea.model.response.CommanderProblem;
import hu.zza.iotea.service.connection.Sender;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

@Service
public class DeviceCommander implements Commander {
  private final Logger logger = LoggerFactory.getLogger(DeviceCommander.class);
  private Sender sender;

  @Autowired
  @Qualifier("telnetSender")
  public void setSender(Sender sender) {
    this.sender = sender;
  }

  public String sendPayload(Device device, String payload) {
    try {
      return sender.send(device.getAddress(), device.getPort(), payload);

    } catch (Exception exception) {
      var message =
          "Cannot send command to Addressable(address=%s, port=%d)"
              .formatted(device.getAddress(), device.getPort());
      logger.warn(message, exception);
      throw new CommanderProblem(message);
    }
  }
}
