package hu.zza.iotea.repository;

import hu.zza.iotea.model.Job;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface JobRepository extends JpaRepository<Job, Integer> {
  Optional<Job> findByName(String name);

  void deleteByName(String name);
}
