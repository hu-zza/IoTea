package hu.zza.bulbman.service.connection;

import hu.zza.bulbman.model.DeviceAddress;

public interface Sender {
  String send(DeviceAddress address, int port, String payload);
}
