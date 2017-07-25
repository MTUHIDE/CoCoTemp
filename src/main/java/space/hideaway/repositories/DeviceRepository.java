package space.hideaway.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import space.hideaway.model.Device;

import java.util.UUID;

/**
 * Created by Justin on 6/8/2017.
 */
public interface DeviceRepository extends JpaRepository<Device, UUID> {
    @Query("SELECT count(d) from Device d where d.userID=:id")
    Long countByUserID(@Param("id") Long id);
}
