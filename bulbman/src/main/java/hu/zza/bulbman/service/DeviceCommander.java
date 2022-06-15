package hu.zza.bulbman.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import hu.zza.bulbman.model.Device;
import hu.zza.bulbman.model.response.*;
import hu.zza.bulbman.service.connection.Sender;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

@Service
public class DeviceCommander {
  private Logger logger = LoggerFactory.getLogger(DeviceCommander.class);
  private Sender sender;
  private ObjectMapper objectMapper;

  private Class<? extends Response> responseType;

  {
    setResponseType(YeelightResponse.class);
  }

  @Autowired
  @Qualifier("telnetSender")
  public void setSender(Sender sender) {
    this.sender = sender;
  }

  @Autowired
  public void setObjectMapper(ObjectMapper objectMapper) {
    this.objectMapper = objectMapper;
  }

  public void setResponseType(Class<? extends Response> responseType) {
    this.responseType = responseType;
  }

  public Response sendPayload(Device device, String payload) {
    try {
      String rawResponse = sender.send(device.getAddress(), device.getPort(), payload);
      return objectMapper.readValue(rawResponse, responseType);

    } catch (Exception exception) {
      var message =
          "Cannot send command to Addressable(address=%s, port=%d)"
              .formatted(device.getAddress(), device.getPort());
      logger.warn(message, exception);
      return new CommanderProblem(message);
    }
  }
}
