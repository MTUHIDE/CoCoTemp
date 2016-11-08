package space.hideaway.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import space.hideaway.model.Data;

import java.util.UUID;

/**
 * Created by dough on 11/1/2016.
 */
public interface DataRepository extends JpaRepository<Data, UUID> {
}
