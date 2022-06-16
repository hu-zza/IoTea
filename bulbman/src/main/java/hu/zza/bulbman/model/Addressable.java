package hu.zza.bulbman.model;

public interface Addressable {
  String getId();

  void setId(String id);

  String getName();

  void setName(String name);

  DeviceAddress getAddress();

  void setAddress(DeviceAddress address);

  int getPort();

  void setPort(int port);
}
