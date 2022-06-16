package hu.zza.bulbman.repository;

import hu.zza.bulbman.model.Command;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CommandRepository extends JpaRepository<Command, String> {}
