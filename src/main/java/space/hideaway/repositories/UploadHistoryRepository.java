package space.hideaway.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import space.hideaway.model.UploadHistory;

import java.util.UUID;


public interface UploadHistoryRepository extends JpaRepository<UploadHistory, UUID> {
}
