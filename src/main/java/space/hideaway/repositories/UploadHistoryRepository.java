package space.hideaway.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import space.hideaway.model.UploadHistory;

import java.util.UUID;

/**
 * Created by dough on 11/20/2016.
 */
public interface UploadHistoryRepository extends JpaRepository<UploadHistory, UUID> {
}
