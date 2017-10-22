package space.hideaway.model.upload;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import org.hibernate.annotations.GenericGenerator;
import space.hideaway.model.Device;
import space.hideaway.model.site.Site;

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

    private UUID id;

    private UUID siteID;
    private Site site;

    private UUID deviceID;
    private Device device;

    private int userID;

    private boolean viewed;
    private boolean error;

    private Date dateTime;
    private Long duration;
    private String description;
    private Integer records;

    /**
     * Gets id.
     *
     * @return the id
     */
    @JsonProperty("id")
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
    @JsonIgnore
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
    @JsonProperty("siteID")
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
     * Gets user id.
     *
     * @return the user id.
     */
    @JsonProperty
    @Column(name = "user_id")
    public int getUserID()
    {
        return userID;
    }

    /**
     * Sets user id.
     *
     * @param userID the user id.
     */
    public void setUserID(int userID)
    {
        this.userID = userID;
    }

    /**
     * Gets error.
     *
     * @return the error.
     */
    @Column(name = "error")
    @JsonProperty("error")
    public boolean isError()
    {
        return error;
    }

    /**
     * Sets error.
     *
     * @param error the error
     */
    public void setError(boolean error)
    {
        this.error = error;
    }

    /**
     * Gets date time.
     *
     * @return the date time
     */
    @JsonProperty("dateTime")
    @Temporal(value = TemporalType.TIMESTAMP)
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
    @JsonProperty("duration")
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
    @JsonProperty("description")
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

    /**
     * Gets records.
     *
     * @return the records
     */
    @JsonProperty("records")
    @Column(name = "records")
    public Integer getRecords()
    {
        return records;
    }

    /**
     * Sets records.
     *
     * @param records the records
     */
    public void setRecords(Integer records)
    {
        this.records = records;
    }

    /**
     * Gets viewed.
     *
     * @return the viewed
     */
    @Column(name = "viewed")
    @JsonProperty("viewed")
    public boolean isViewed()
    {
        return viewed;
    }

    /**
     * Sets viewed.
     *
     * @param viewed the
     */
    public void setViewed(boolean viewed)
    {
        this.viewed = viewed;
    }

    /**
     * Gets device id.
     *
     * @return the device id
     */
    @Column(name = "device_id", length = 16)
    public UUID getDeviceID() {
        return deviceID;
    }

    /**
     * Sets device id.
     *
     * @param deviceID the device id
     */
    public void setDeviceID(UUID deviceID) {
        this.deviceID = deviceID;
    }

    /**
     * Gets device.
     *
     * @return the device
     */
    @ManyToOne()
    @JsonIgnore
    @JoinColumn(name = "device_id", insertable = false, updatable = false)
    public Device getDevice()
    {
        return device;
    }

    /**
     * Sets device.
     *
     * @param device the device
     */
    public void setDevice(Device device)
    {
        this.device = device;
    }
}
