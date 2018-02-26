package space.hideaway.repositories.site;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import space.hideaway.model.site.SiteMetadata;

import java.util.UUID;

@Repository
public interface SiteMetadataRepository extends JpaRepository<SiteMetadata, UUID> {

}
