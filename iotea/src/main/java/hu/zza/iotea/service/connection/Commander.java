package hu.zza.iotea.service.connection;

import hu.zza.iotea.model.Device;

public interface Commander {
  String sendPayload(Device device, String payload);
}
