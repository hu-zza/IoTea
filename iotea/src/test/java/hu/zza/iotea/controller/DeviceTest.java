package hu.zza.iotea.controller;

import static org.junit.jupiter.api.Assertions.assertEquals;

import hu.zza.iotea.model.Device;
import hu.zza.iotea.model.DeviceAddress;
import org.junit.jupiter.api.Test;

class DeviceTest {
  private final Device deviceA = new Device(11, "u11", "n11", new DeviceAddress("192.168.0.1"), 10);
  private final Device deviceB = new Device(11, "u22", "n22", new DeviceAddress("192.168.0.2"), 20);

  @Test
  void equals() {
    assertEquals(deviceA, deviceB);
  }
}
