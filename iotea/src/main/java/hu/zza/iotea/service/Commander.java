package hu.zza.iotea.service;

import hu.zza.iotea.model.Device;

public interface Commander {
  String sendPayload(Device device, String payload);
}
