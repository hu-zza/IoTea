package hu.zza.iotea.model.dto;

import hu.zza.iotea.model.Identifiable;
import lombok.Data;

@Data
public class DeviceOutput implements Identifiable {
  private Integer id;
  private String uid;
  private String name;
  private DeviceAddressOutput address;
  private Integer port;
}
