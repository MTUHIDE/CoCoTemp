package space.hideaway.model;

import javax.persistence.*;
import java.util.Set;


/**
 * The type Role.
 */
@Entity
@Table(name = "role")
public class Role {

    private Long id;

    private String name;

    private Set<User> userSet;

    /**
     * Gets id.
     *
     * @return the id
     */
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    public Long getId() {
        return id;
    }

    /**
     * Sets id.
     *
     * @param id the id
     */
    public void setId(Long id) {
        this.id = id;
    }

    /**
     * Gets name.
     *
     * @return the name
     */
    public String getName() {
        return name;
    }

    /**
     * Sets name.
     *
     * @param name the name
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * Gets user set.
     *
     * @return the user set
     */
    @ManyToMany(mappedBy = "roleSet")
    public Set<User> getUserSet() {
        return userSet;
    }

    /**
     * Sets user set.
     *
     * @param userSet the user set
     */
    public void setUserSet(Set<User> userSet) {
        this.userSet = userSet;
    }
}
