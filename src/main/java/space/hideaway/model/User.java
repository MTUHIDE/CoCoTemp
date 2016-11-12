package space.hideaway.model;

import javax.persistence.*;
import java.util.Set;

/**
 * HIDE CoCoTemp 2016
 * <p>
 * JPA model for representing a user.
 *
 * @author Piper Dougherty
 */
@Entity
@Table(name = "user")
public class User {

    /**
     * The ID of the user in the database.
     */
    private Long id;

    /**
     * The username of the user.
     */
    private String username;

    /**
     * The password of the user.
     */
    private String password;

    /**
     * The secondary password of the user, used for form validation.
     */
    private String confirmationPassword;

    /**
     * The set of roles this user has permissions for.
     */
    private Set<Role> roleSet;

    /**
     * The set of devices this user maintains.
     */
    private Set<Device> deviceSet;

    /**
     * Get the ID of this user.
     *
     * @return The ID of this user.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    public Long getId() {
        return id;
    }

    /**
     * Set the ID of this user.
     *
     * @param id The new ID of this user.
     */
    public void setId(Long id) {
        this.id = id;
    }

    /**
     * Get the username for this user.
     *
     * @return The username for this user.
     */
    public String getUsername() {
        return username;
    }

    /**
     * Set the username for this user.
     *
     * @param username The new username for this user.
     */
    public void setUsername(String username) {
        this.username = username;
    }

    /**
     * Get the password for this user.
     *
     * @return The password for this user.
     */
    public String getPassword() {
        return password;
    }

    /**
     * Set the password for this user.
     *
     * @param password The new password for this user.
     */
    public void setPassword(String password) {
        this.password = password;
    }

    /**
     * Get the confirmation password for this user. Used in form validation
     * to ensure that the user entered the same password twice.
     *
     * @return The confirmation password for this user.
     */
    @Transient
    public String getConfirmationPassword() {
        return confirmationPassword;
    }

    /**
     * Sets confirmation password.
     *
     * @param confirmationPassword the confirmation password
     */
    public void setConfirmationPassword(String confirmationPassword) {
        this.confirmationPassword = confirmationPassword;
    }

    /**
     * Get the set of roles this user has permissions for.
     *
     * @return A set of roles this user has permissions for.
     */
    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(name = "user_role", joinColumns = @JoinColumn(name = "user_id"), inverseJoinColumns = @JoinColumn(name = "role_id"))
    public Set<Role> getRoleSet() {
        return roleSet;
    }

    /**
     * Sets role set.
     *
     * @param roleSet the role set
     */
    public void setRoleSet(Set<Role> roleSet) {
        this.roleSet = roleSet;
    }

    /**
     * Get the set of devices this user maintains.
     *
     * @return A set of devices this user maintains.
     */
    @OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinColumn(name = "user_id")
    public Set<Device> getDeviceSet() {
        return deviceSet;
    }

    /**
     * Set the set of devices this user maintains.
     *
     * @param deviceSet The new set of devices.
     */
    public void setDeviceSet(Set<Device> deviceSet) {
        this.deviceSet = deviceSet;
    }
}
