package hu.zza.bulbman.model;

import java.net.InetAddress;

public interface DeviceAddress {
  InetAddress NULL_IP = InetAddress.getLoopbackAddress();
  InetAddress getIp();
  void setIp(InetAddress ip);
}
