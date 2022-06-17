package hu.zza.iotea.service.connection;

import hu.zza.iotea.model.DeviceAddress;

public interface Sender {
  String send(DeviceAddress address, int port, String payload);
}
