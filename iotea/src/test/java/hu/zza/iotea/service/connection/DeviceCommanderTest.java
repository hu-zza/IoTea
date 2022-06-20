package hu.zza.iotea.service.connection;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import hu.zza.iotea.model.Device;
import hu.zza.iotea.model.DeviceAddress;
import hu.zza.iotea.model.exception.ConnectionProblem;
import org.junit.jupiter.api.Test;

class DeviceCommanderTest {

  @Test
  void sendPayload() {
    var commander = new DeviceCommander();
    commander.setSender(new TelnetSender());
    var exception =
        assertThrows(
            ConnectionProblem.class,
            () ->
                commander.sendPayload(
                    new Device(3, "U-3", "N3", new DeviceAddress(DeviceAddress.NULL_IP), 80),
                    "..."));

    assertEquals(
        "Connection exception: Cannot send command to Device (ID: 3, UID: U-3, name: N3)",
        exception.getMessage());
  }
}
