package space.hideaway.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import space.hideaway.model.globe.Globe;

import java.util.UUID;

public interface GlobeRepository extends JpaRepository<Globe, UUID> {
}
