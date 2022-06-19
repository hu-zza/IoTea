package hu.zza.iotea.repository;

import hu.zza.iotea.model.Job;
import java.util.Optional;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;

public interface JobRepository extends JpaRepository<Job, Integer> {
  @Query("SELECT d.id FROM Device d WHERE name = :name")
  Optional<Integer> getIdByName(String name);

  Optional<Job> findByName(String name);

  @Modifying
  @Query(
      value =
          "INSERT INTO jobs VALUES (:#{#e.id}, :#{#e.name}, :#{#e.device.id}, :#{#e.command.id})",
      nativeQuery = true)
  void insertWithId(@Param("e") Job entity);

  void deleteByName(String name);
}
