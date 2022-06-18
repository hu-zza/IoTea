package hu.zza.iotea.repository;

import hu.zza.iotea.model.Device;
import java.util.Optional;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface DeviceRepository extends JpaRepository<Device, Integer> {
  @Query("SELECT d.id FROM Device d WHERE uid = :uid")
  Optional<Integer> getIdByUid(String uid);

  @Query("SELECT d.id FROM Device d WHERE name = :name")
  Optional<Integer> getIdByName(String name);

  Optional<Device> findByUid(String uid);

  Optional<Device> findByName(String name);

  @Modifying
  @Query(
      value =
          "INSERT INTO devices VALUES (:#{#e.id}, :#{#e.uid}, :#{#e.name}, :#{#e.address.getAsDatabaseColumn()}, :#{#e.port})",
      nativeQuery = true)
  void insertWithId(@Param("e") Device entity);

  void deleteByUid(String uid);

  void deleteByName(String name);
}
