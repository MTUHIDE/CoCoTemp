package space.hideaway.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import space.hideaway.model.Device;

import java.util.UUID;

/**
 * Created by Justin on 6/8/2017.
 */
@Repository
public interface DeviceRepository extends JpaRepository<Device, UUID>
{
    /**
     * Gets the count of devices created by an user.
     *
     * @param id The user id
     * @return The number of devices.
     */
    @Query("SELECT count(d) from Device d where d.userID=:id")
    Long countByUserID(@Param("id") Long id);
}
