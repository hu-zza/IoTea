package hu.zza.iotea.repository;

import hu.zza.iotea.model.Command;
import hu.zza.iotea.model.dto.CommandUpdate;
import java.util.Optional;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface CommandRepository extends JpaRepository<Command, Integer> {
  @Query("SELECT c.id FROM Command c WHERE name = :name")
  Optional<Integer> getIdByName(String name);

  Optional<Command> findByName(String name);

  @Modifying
  @Query(
      value = "INSERT INTO commands VALUES (:#{#e.id}, :#{#e.name}, :#{#e.template}, :#{#e.note})",
      nativeQuery = true)
  void insertWithId(@Param("e") Command entity);

  void deleteByName(String name);
}
