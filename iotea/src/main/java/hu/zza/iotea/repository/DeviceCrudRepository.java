package hu.zza.iotea.repository;

import hu.zza.iotea.model.Device;
import org.springframework.data.repository.CrudRepository;
;

public interface DeviceCrudRepository extends CrudRepository<Device, Integer> {}
