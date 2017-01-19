package space.hideaway.model;

import javax.persistence.*;
import java.util.Set;


/**
 * The type User.
 */
@Entity
@Table(name = "user")
public class User {

    private Long id;

    private String username;

    private String password;

    private String confirmationPassword;

    private Set<Role> roleSet;

    private Set<Device> deviceSet;

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
     * Gets username.
     *
     * @return the username
     */
    public String getUsername() {
        return username;
    }

    /**
     * Sets username.
     *
     * @param username the username
     */
    public void setUsername(String username) {
        this.username = username;
    }

    /**
     * Gets password.
     *
     * @return the password
     */
    public String getPassword() {
        return password;
    }

    /**
     * Sets password.
     *
     * @param password the password
     */
    public void setPassword(String password) {
        this.password = password;
    }

    /**
     * Gets confirmation password.
     *
     * @return the confirmation password
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
     * Gets role set.
     *
     * @return the role set
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
     * Gets device set.
     *
     * @return the device set
     */
    @OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinColumn(name = "user_id")
    public Set<Device> getDeviceSet() {
        return deviceSet;
    }

    /**
     * Sets device set.
     *
     * @param deviceSet the device set
     */
    public void setDeviceSet(Set<Device> deviceSet) {
        this.deviceSet = deviceSet;
    }
}
