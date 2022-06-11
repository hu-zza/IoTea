package hu.zza.bulbman.model;

import java.io.Serializable;
import java.net.InetAddress;

public interface DeviceAddress extends Serializable {
  InetAddress getIp();

  void setIp(InetAddress ip);
}
