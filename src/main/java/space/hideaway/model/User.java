package space.hideaway.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import org.apache.lucene.analysis.core.KeywordAnalyzer;
import org.hibernate.search.annotations.Analyze;
import org.hibernate.search.annotations.Analyzer;
import org.hibernate.search.annotations.Field;
import org.hibernate.search.annotations.Store;

import javax.persistence.*;
import java.util.Set;


/**
 * The type User.
 */
@Entity
@Table(name = "user")
public class User
{

    @JsonIgnore
    private Long id;

    private String email;

    @Field(index = org.hibernate.search.annotations.Index.YES, analyze = Analyze.YES, store = Store.NO)
    @Analyzer(impl = KeywordAnalyzer.class)
    private String username;

    @JsonIgnore
    private String password;

    private String firstName;

    private String middleInitial;

    private String lastName;

    private String confirmationPassword;

    private Set<Role> roleSet;

    private Set<Site> siteSet;

    private Set<UploadHistory> uploadHistorySet;

    private Set<Device> deviceSet;

    /**
     * Gets id.
     *
     * @return the id
     */
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    public Long getId()
    {
        return id;
    }

    /**
     * Sets id.
     *
     * @param id the id
     */
    public void setId(Long id)
    {
        this.id = id;
    }

    @Column(name = "email")
    public String getEmail()
    {
        return email;
    }

    public void setEmail(String email)
    {
        this.email = email;
    }

    /**
     * Gets username.
     *
     * @return the username
     */
    public String getUsername()
    {
        return username;
    }

    /**
     * Sets username.
     *
     * @param username the username
     */
    public void setUsername(String username)
    {
        this.username = username;
    }

    /**
     * Gets password.
     *
     * @return the password
     */
    public String getPassword()
    {
        return password;
    }

    /**
     * Sets password.
     *
     * @param password the password
     */
    public void setPassword(String password)
    {
        this.password = password;
    }

    /**
     * Gets confirmation password.
     *
     * @return the confirmation password
     */
    @Transient
    public String getConfirmationPassword()
    {
        return confirmationPassword;
    }

    /**
     * Sets confirmation password.
     *
     * @param confirmationPassword the confirmation password
     */
    public void setConfirmationPassword(String confirmationPassword)
    {
        this.confirmationPassword = confirmationPassword;
    }

    @Column(name = "first_name")
    public String getFirstName()
    {
        return firstName;
    }

    public void setFirstName(String firstName)
    {
        this.firstName = firstName;
    }

    @Column(name = "middle_initial")
    public String getMiddleInitial()
    {
        return middleInitial;
    }

    public void setMiddleInitial(String middleInitial)
    {
        this.middleInitial = middleInitial;
    }

    @Column(name = "last_name")
    public String getLastName()
    {
        return lastName;
    }

    public void setLastName(String lastName)
    {
        this.lastName = lastName;
    }

    /**
     * Gets role set.
     *
     * @return the role set
     */
    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(name = "user_role", joinColumns = @JoinColumn(name = "user_id"), inverseJoinColumns = @JoinColumn(name
            = "role_id"))
    public Set<Role> getRoleSet()
    {
        return roleSet;
    }

    /**
     * Sets role set.
     *
     * @param roleSet the role set
     */
    public void setRoleSet(Set<Role> roleSet)
    {
        this.roleSet = roleSet;
    }

    /**
     * Gets site set.
     *
     * @return the site set
     */
    @OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinColumn(name = "user_id")
    public Set<Site> getSiteSet()
    {
        return siteSet;
    }

    /**
     * Sets site set.
     *
     * @param siteSet the site set
     */
    public void setSiteSet(Set<Site> siteSet)
    {
        this.siteSet = siteSet;
    }

    @OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinColumn(name = "user_id")
    public Set<UploadHistory> getUploadHistorySet()
    {
        return uploadHistorySet;
    }

    public void setUploadHistorySet(Set<UploadHistory> uploadHistorySet)
    {
        this.uploadHistorySet = uploadHistorySet;
    }


    @OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinColumn(name = "user_id")
    public Set<Device> getDeviceSet() {
        return deviceSet;
    }

    public void setDeviceSet(Set<Device> deviceSet) {
        this.deviceSet = deviceSet;
    }
}
