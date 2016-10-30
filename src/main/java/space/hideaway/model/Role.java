package space.hideaway.model;

import javax.persistence.*;
import java.util.Set;

/**
 * HIDE CoCoTemp 2016
 *
 * JPA model for representing the role of a user by way of foreign keys.
 *
 * @author Piper Dougherty
 */
@Entity
@Table(name = "role")
public class Role {

    private Long id;

    /**
     * The name of the role. Currently implemented roles are
     * ROLE_USER and ROLE_ADMIN.
     */
    private String name;

    /**
     * A set of all users who have this role.
     */
    private Set<User> userSet;

    /**
     * Get the ID of this role.
     *
     * @return The ID of this role.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    public Long getId() {
        return id;
    }

    /**
     * Set the ID of this role. Should probably not be used...
     * @param id The new ID of this role.
     */
    public void setId(Long id) {
        this.id = id;
    }

    /**
     * Get the name of this role.
     * @return The name of the role.
     */
    public String getName() {
        return name;
    }

    /**
     * Set the name of this role.
     * @param name The new name of this role.
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * Get the set of users that have this role.
     * @return A set of users that have this role.
     */
    @ManyToMany(mappedBy = "roleSet")
    public Set<User> getUserSet() {
        return userSet;
    }

    /**
     * Set the set of users that have this role.
     *
     * @param userSet The new set of user that have this role.
     */
    public void setUserSet(Set<User> userSet) {
        this.userSet = userSet;
    }
}
