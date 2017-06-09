package space.hideaway.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.util.Date;
import java.util.UUID;


/**
 * The type Upload history.
 */
@Table(name = "upload_history")
@Entity
@JsonPropertyOrder({"dateTime"})
public class UploadHistory
{

    /**
     * The Id.
     */
    @JsonProperty("id")
    UUID id;

    /**
     * The Site id.
     */
    @JsonProperty("siteID")
    UUID siteID;


    @JsonProperty
    int userID;


    /**
     * The Site.
     */
    @JsonIgnore
    Site site;


    @JsonProperty("viewed")
    boolean viewed;

    @JsonProperty("error")
    boolean error;

    private UUID deviceID;

    @JsonIgnore
    private Device device;

    /**
     * The Date time.
     */
    @JsonProperty("dateTime")
    @Temporal(value = TemporalType.TIMESTAMP)
    Date dateTime;

    /**
     * The Duration.
     */
    @JsonProperty("duration")
    Long duration;

    /**
     * The Description.
     */
    @JsonProperty("description")
    String description;

    @JsonProperty("records")
    Integer records;

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
     * Gets site.
     *
     * @return the site
     */
    @ManyToOne(fetch = FetchType.LAZY)
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

    @Column(name = "user_id")
    public int getUserID()
    {
        return userID;
    }

    public void setUserID(int userID)
    {
        this.userID = userID;
    }

    @Column(name = "error")
    public boolean isError()
    {
        return error;
    }

    public void setError(boolean error)
    {
        this.error = error;
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
     * Gets duration.
     *
     * @return the duration
     */
    @Column(name = "duration")
    public Long getDuration()
    {
        return duration;
    }

    /**
     * Sets duration.
     *
     * @param duration the duration
     */
    public void setDuration(Long duration)
    {
        this.duration = duration;
    }

    /**
     * Gets description.
     *
     * @return the description
     */
    @Column(name = "description")
    public String getDescription()
    {
        return description;
    }

    /**
     * Sets description.
     *
     * @param description the description
     */
    public void setDescription(String description)
    {
        this.description = description;
    }

    @Column(name = "records")
    public Integer getRecords()
    {
        return records;
    }

    public void setRecords(Integer records)
    {
        this.records = records;
    }

    @Column(name = "viewed")
    public boolean isViewed()
    {
        return viewed;
    }

    public void setViewed(boolean viewed)
    {
        this.viewed = viewed;
    }

    //____________________________________________________________

    @Column(name = "device_id", length = 16)
    public UUID getDeviceID() {
        return deviceID;
    }

    public void setDeviceID(UUID deviceID) {
        this.siteID = deviceID;
    }

    @ManyToOne()
    @JoinColumn(name = "device_id", insertable = false, updatable = false)
    public Device getDevice()
    {
        return device;
    }

    public void setDevice(Device device)
    {
        this.device = device;
    }
}
