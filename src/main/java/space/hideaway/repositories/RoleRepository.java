package space.hideaway.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import space.hideaway.model.Role;

/**
 * Created by dough on 10/9/2016.
 */
public interface RoleRepository extends JpaRepository<Role, Long> {
}
