package space.hideaway.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.util.Date;
import java.util.UUID;


/**
 * The type Upload history.
 */
@Table(name = "upload_history")
@Entity
public class UploadHistory {

    /**
     * The Id.
     */
    @JsonProperty("id")
    UUID id;

    /**
     * The Device id.
     */
    @JsonProperty("deviceID")
    UUID deviceID;

    /**
     * The Device.
     */
    @JsonIgnore
    Device device;


    @JsonProperty("viewed")
    boolean viewed;

    @JsonProperty("error")
    boolean error;

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

    /**
     * Gets id.
     *
     * @return the id
     */
    @GeneratedValue(generator = "uuid2")
    @GenericGenerator(name = "uuid2", strategy = "uuid2")
    @Id
    @Column(name = "id")
    public UUID getId() {
        return id;
    }

    /**
     * Sets id.
     *
     * @param id the id
     */
    public void setId(UUID id) {
        this.id = id;
    }

    /**
     * Gets device.
     *
     * @return the device
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "device_id", insertable = false, updatable = false)
    public Device getDevice() {
        return device;
    }

    /**
     * Sets device.
     *
     * @param device the device
     */
    public void setDevice(Device device) {
        this.device = device;
    }

    /**
     * Gets device id.
     *
     * @return the device id
     */
    @Column(name = "device_id")
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

    @Column(name = "error")
    public boolean isError() {
        return error;
    }

    public void setError(boolean error) {
        this.error = error;
    }

    /**
     * Gets date time.
     *
     * @return the date time
     */
    @Column(name = "date")
    public Date getDateTime() {
        return dateTime;
    }

    /**
     * Sets date time.
     *
     * @param dateTime the date time
     */
    public void setDateTime(Date dateTime) {
        this.dateTime = dateTime;
    }

    /**
     * Gets duration.
     *
     * @return the duration
     */
    @Column(name = "duration")
    public Long getDuration() {
        return duration;
    }

    /**
     * Sets duration.
     *
     * @param duration the duration
     */
    public void setDuration(Long duration) {
        this.duration = duration;
    }

    /**
     * Gets description.
     *
     * @return the description
     */
    @Column(name = "description")
    public String getDescription() {
        return description;
    }

    /**
     * Sets description.
     *
     * @param description the description
     */
    public void setDescription(String description) {
        this.description = description;
    }

    @Column(name = "viewed")
    public boolean isViewed() {
        return viewed;
    }

    public void setViewed(boolean viewed) {
        this.viewed = viewed;
    }
}
