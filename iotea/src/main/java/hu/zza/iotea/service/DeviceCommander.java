package hu.zza.iotea.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import hu.zza.iotea.model.Device;
import hu.zza.iotea.model.response.CommanderProblem;
import hu.zza.iotea.model.response.Response;
import hu.zza.iotea.service.connection.Sender;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

@Service
public class DeviceCommander {
  private final Logger logger = LoggerFactory.getLogger(DeviceCommander.class);
  private Sender sender;
  private ObjectMapper objectMapper;

  @Autowired
  @Qualifier("telnetSender")
  public void setSender(Sender sender) {
    this.sender = sender;
  }

  @Autowired
  public void setObjectMapper(ObjectMapper objectMapper) {
    this.objectMapper = objectMapper;
  }

  public Response sendPayload(Device device, String payload) {
    try {
      String rawResponse = sender.send(device.getAddress(), device.getPort(), payload);
      return objectMapper.readValue(rawResponse, Response.class);

    } catch (Exception exception) {
      var message =
          "Cannot send command to Addressable(address=%s, port=%d)"
              .formatted(device.getAddress(), device.getPort());
      logger.warn(message, exception);
      return new CommanderProblem(device.getId(), 0L, message);
    }
  }
}
