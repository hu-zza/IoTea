package hu.zza.iotea.model;

import static org.junit.jupiter.api.Assertions.assertFalse;

import org.junit.jupiter.api.Test;

class DeviceAddressTest {

  @Test
  void isReachable() {
    var addrA = new DeviceAddress("10.20.30.40");
    assertFalse(addrA.isReachable());

    var addrB = new DeviceAddress(DeviceAddress.NULL_IP);
    assertFalse(addrB.isReachable());
  }
}
