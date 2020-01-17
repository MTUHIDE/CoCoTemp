package space.hideaway.repositories.site;


import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import org.springframework.data.repository.query.Param;
import space.hideaway.model.site.Site;


import java.util.Optional;
import java.util.UUID;

public interface SiteRepository extends JpaRepository<Site, UUID>
{
    /**
     * Gets the count of sites created by an user.
     *
     * @param id The user id
     * @return The number of sites.
     */
    @Query("SELECT count(d) from Site d where d.userID=:id")
    Long countByUserID(@Param("id") Long id);

    Optional<Site> findById(UUID id);

}
