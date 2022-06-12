package hu.zza.bulbman.model;

public interface Device {
  String getId();

  void setId(String id);

  String getName();

  void setName(String name);

  DeviceAddress getAddress();

  void setAddress(DeviceAddress address);
}
