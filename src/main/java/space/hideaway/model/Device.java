package space.hideaway.model;

import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.util.Set;
import java.util.UUID;

/**
 * HIDE CoCoTemp 2016
 * <p>
 * JPA model for user devices. Used for representing SQL table data in POJO format. Also responsible for
 * mapping SQL table columns to their respective class fields.
 *
 * @author Piper Dougherty
 */
@Entity
@Table(name = "device")
public class Device {

    /**
     * The ID of the device, generated sequentially by the database.
     */
    private UUID id;

    /**
     * The ID of the device owner.
     */
    private Long userId;


    private String deviceName;

    private double deviceLatitude;
    private double deviceLongitude;

    private Set<Data> dataSet;


    public Device(String deviceName, double deviceLatitude, double deviceLongitude) {
        this.deviceName = deviceName;
        this.deviceLatitude = deviceLatitude;
        this.deviceLongitude = deviceLongitude;
    }

    public Device() {
    }


    /**
     * Get the ID for the user in the database.
     *
     * @return The ID of the user.
     */
    @GeneratedValue(generator = "uuid2")
    @GenericGenerator(name = "uuid2", strategy = "uuid2")
    @Id
    @Column(name = "id")
    public UUID getId() {
        return id;
    }

    /**
     * Set the ID for this device in the database.
     *
     * @param id The new ID for this device.
     */
    public void setId(UUID id) {
        this.id = id;
    }

    /**
     * Obtain the ID of this device's owner.
     *
     * @return The ID of this device's owner.
     */
    @Column(name = "user_id")
    public Long getUserId() {
        return userId;
    }

    /**
     * Set the ID of this device's owner. Possible use is migration of device from
     * one user to another.
     *
     * @param userId The new user ID.
     */
    public void setUserId(Long userId) {
        this.userId = userId;
    }

    /**
     * Get the name of this device.
     *
     * @return The name of this device.
     */
    @Column(name = "device_name")
    public String getDeviceName() {
        return deviceName;
    }


    /**
     * Set the name of this device. Possible use is device rename.
     *
     * @param deviceName The new name of the device.
     */
    public void setDeviceName(String deviceName) {
        this.deviceName = deviceName;
    }

    /**
     * Get the string representation of the location of this device.
     * TODO: How could one convert a string representation of the location into a point on a map? Possibly Google Geocode.
     *
     * @return The string representation of the location of this device.
     */
    @Column(name = "device_latitude")
    public double getDeviceLatitude() {
        return deviceLatitude;
    }

    /**
     * Set the string representation of the location of this device.
     *
     * @param deviceLatitude The new location of this device.
     */
    public void setDeviceLatitude(double deviceLatitude) {
        this.deviceLatitude = deviceLatitude;
    }


    @Column(name = "device_longitude")
    public double getDeviceLongitude() {
        return deviceLongitude;
    }

    public void setDeviceLongitude(double deviceLongitude) {
        this.deviceLongitude = deviceLongitude;
    }

    @OneToMany(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    @JoinColumn(name = "device_id")
    public Set<Data> getDataSet() {
        return dataSet;
    }

    public void setDataSet(Set<Data> dataSet) {
        this.dataSet = dataSet;
    }

    @Override
    public String toString() {
        return String.format(
                "Device: [ID: %s Name: %s Location: %s]%n",
                getId(),
                getDeviceName(),
                getDeviceLatitude());
    }
}
