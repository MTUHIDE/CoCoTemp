package space.hideaway.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import space.hideaway.model.User;


public interface UserRepository extends JpaRepository<User, Long>
{

    /**
     * Obtain a user matching a given username.
     *
     * @param username The username of the user to obtain.
     *
     *                @return A user represented by the given username.
     */
    User findByUsername(String username);

    /**
     * Obtain a user matching a given email.
     *
     * @param email The email of the user to obtain.
     * @return A user represented by the given email.
     */
    User findByEmail(String email);




}
