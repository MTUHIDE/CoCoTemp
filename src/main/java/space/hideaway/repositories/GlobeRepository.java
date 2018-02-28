package space.hideaway.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import space.hideaway.model.globe.Globe;

import java.util.UUID;

@Repository
public interface GlobeRepository extends JpaRepository<Globe, UUID> {

}
