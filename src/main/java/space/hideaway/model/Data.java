package space.hideaway.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonView;
import org.hibernate.annotations.GenericGenerator;
import org.springframework.data.jpa.datatables.mapping.DataTablesOutput;

import javax.persistence.*;
import java.util.Date;
import java.util.UUID;

/**
 * The type Data.
 */
@Entity
@Table(name = "data")
public class Data {

    private UUID id;

    private int userID;

    private User user;

    private UUID deviceID;

    @JsonBackReference
    private Device device;

    @Temporal(value = TemporalType.TIMESTAMP)
    @JsonView(DataTablesOutput.View.class)
    private Date dateTime;

    @JsonView(DataTablesOutput.View.class)
    private double temperature;


    /**
     * Instantiates a new Data.
     */
    public Data() {
    }


    /**
     * Instantiates a new Data.
     *
     * @param id          the id
     * @param deviceID    the device id
     * @param dateTime    the date time
     * @param temperature the temperature
     */
    public Data(UUID id, UUID deviceID, Date dateTime, double temperature) {
        this.id = id;
        this.deviceID = deviceID;
        this.dateTime = dateTime;
        this.temperature = temperature;
    }

    /**
     * Gets device.
     *
     * @return the device
     */
    @ManyToOne()
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

    @Override
    public String toString() {
        return String.format(
                "Data: [ID: %s Device ID: %s Date: %s Temperature: %s]%n",
                getId().toString(),
                getDeviceID(),
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
     * Gets temperature.
     *
     * @return the temperature
     */
    @Column(name = "temperature")
    public double getTemperature() {
        return temperature;
    }

    /**
     * Sets temperature.
     *
     * @param temperature the temperature
     */
    public void setTemperature(double temperature) {
        this.temperature = temperature;
    }

    /**
     * Gets user id.
     *
     * @return the user id
     */
    @Column(name = "user_id")
    public int getUserID() {
        return userID;
    }

    /**
     * Sets user id.
     *
     * @param userID the user id
     */
    public void setUserID(int userID) {
        this.userID = userID;
    }

    /**
     * Gets user.
     *
     * @return the user
     */
    @ManyToOne()
    @JoinColumn(name = "user_id", insertable = false, updatable = false)
    public User getUser() {
        return user;
    }

    /**
     * Sets user.
     *
     * @param user the user
     */
    public void setUser(User user) {
        this.user = user;
    }
}
