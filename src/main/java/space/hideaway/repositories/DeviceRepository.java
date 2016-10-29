package space.hideaway.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import space.hideaway.model.Device;

public interface DeviceRepository extends JpaRepository<Device, Long> {
}
