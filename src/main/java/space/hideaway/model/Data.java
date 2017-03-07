package space.hideaway.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.util.Date;
import java.util.UUID;

/**
 * The type Data.
 */
@Entity
@Table(name = "data")
public class Data
{

    private UUID id;

    private int userID;

    @JsonIgnore
    private User user;

    private UUID siteID;

    @JsonIgnore
    private Site site;

    @Temporal(value = TemporalType.TIMESTAMP)
    private Date dateTime;

    private double temperature;


    /**
     * Instantiates a new Data.
     */
    public Data()
    {
    }


    /**
     * Instantiates a new Data.
     *
     * @param id          the id
     * @param siteID    the site id
     * @param dateTime    the date time
     * @param temperature the temperature
     */
    public Data(UUID id, UUID siteID, Date dateTime, double temperature)
    {
        this.id = id;
        this.siteID = siteID;
        this.dateTime = dateTime;
        this.temperature = temperature;
    }

    /**
     * Gets site.
     *
     * @return the site
     */
    @ManyToOne()
    @JoinColumn(name = "site_id", insertable = false, updatable = false)
    public Site getSite()
    {
        return site;
    }

    /**
     * Sets site.
     *
     * @param site the site
     */
    public void setSite(Site site)
    {
        this.site = site;
    }

    @Override
    public String toString()
    {
        return String.format(
                "Data: [ID: %s Site ID: %s Date: %s Temperature: %s]%n",
                getId().toString(),
                getSiteID(),
                getDateTime(),
                getTemperature()
        );
    }

    /**
     * Gets id.
     *
     * @return the id
     */
    @GeneratedValue(generator = "uuid2")
    @GenericGenerator(name = "uuid2", strategy = "uuid2")
    @Id
    @Column(name = "id", length = 16)
    public UUID getId()
    {
        return id;
    }

    /**
     * Sets id.
     *
     * @param id the id
     */
    public void setId(UUID id)
    {
        this.id = id;
    }

    /**
     * Gets site id.
     *
     * @return the site id
     */
    @Column(name = "site_id", length = 16)
    public UUID getSiteID()
    {
        return siteID;
    }

    /**
     * Sets site id.
     *
     * @param siteID the site id
     */
    public void setSiteID(UUID siteID)
    {
        this.siteID = siteID;
    }

    /**
     * Gets date time.
     *
     * @return the date time
     */
    @Column(name = "date")
    public Date getDateTime()
    {
        return dateTime;
    }

    /**
     * Sets date time.
     *
     * @param dateTime the date time
     */
    public void setDateTime(Date dateTime)
    {
        this.dateTime = dateTime;
    }

    /**
     * Gets temperature.
     *
     * @return the temperature
     */
    @Column(name = "temperature")
    public double getTemperature()
    {
        return temperature;
    }

    /**
     * Sets temperature.
     *
     * @param temperature the temperature
     */
    public void setTemperature(double temperature)
    {
        this.temperature = temperature;
    }

    /**
     * Gets user id.
     *
     * @return the user id
     */
    @Column(name = "user_id")
    public int getUserID()
    {
        return userID;
    }

    /**
     * Sets user id.
     *
     * @param userID the user id
     */
    public void setUserID(int userID)
    {
        this.userID = userID;
    }

    /**
     * Gets user.
     *
     * @return the user
     */
    @ManyToOne()
    @JoinColumn(name = "user_id", insertable = false, updatable = false)
    public User getUser()
    {
        return user;
    }

    /**
     * Sets user.
     *
     * @param user the user
     */
    public void setUser(User user)
    {
        this.user = user;
    }
}
