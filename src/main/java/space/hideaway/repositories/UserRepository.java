package space.hideaway.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import space.hideaway.model.User;

/**
 * Created by dough on 10/9/2016.
 */
public interface UserRepository extends JpaRepository<User, Long> {
    User findByUsername(String username);
}
