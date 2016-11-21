package space.hideaway.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import space.hideaway.model.User;

public interface UserRepository extends JpaRepository<User, Long> {


    User findByUsername(String username);
}
