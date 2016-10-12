package space.hideaway.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import space.hideaway.model.Device;

/**
 * Created by dough on 10/12/2016.
 */
public interface DeviceRepository extends JpaRepository<Device, Long> {

}
