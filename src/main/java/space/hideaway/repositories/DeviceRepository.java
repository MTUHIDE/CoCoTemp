package space.hideaway.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import space.hideaway.model.Device;

import java.util.UUID;

/**
 * Created by Justin on 6/8/2017.
 */
public interface DeviceRepository extends JpaRepository<Device, UUID> {
}
