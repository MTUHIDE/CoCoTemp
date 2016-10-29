package space.hideaway.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import space.hideaway.model.User;

public interface UserRepository extends JpaRepository<User, Long> {

    /**
     * Locate a user in the database by using their username.
     *
     * @param username The username of the user.
     * @return The user that has the username provided.
     */
    User findByUsername(String username);
}
