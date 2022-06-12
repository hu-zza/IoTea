package hu.zza.bulbman.repository.xiaomi.yeelight;

import hu.zza.bulbman.model.xiaomi.yeelight.YeelightDevice;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface YeelightRepository extends JpaRepository<YeelightDevice, String> {}
