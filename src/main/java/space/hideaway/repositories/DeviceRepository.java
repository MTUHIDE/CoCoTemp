package space.hideaway.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import space.hideaway.model.Device;

import java.util.UUID;

public interface DeviceRepository extends JpaRepository<Device, Long> {
    @Query("select x from Device x where x.id=?1")
    Device findByDeviceKey(UUID deviceKey);
}
