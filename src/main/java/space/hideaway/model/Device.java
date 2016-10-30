package space.hideaway.model;

import javax.persistence.*;

/**
 * HIDE CoCoTemp 2016
 *
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
    private Long id;

    /**
     * The ID of the device owner.
     */
    private Long userId;


    private String deviceName;
    private String deviceLocation;

    /**
     * The unique id of the device.
     * TODO: decide how to calculate this value in a way that is scalable and efficient.
     */
    private String deviceUUID;

    public Device(String deviceName, String deviceLocation) {
        this.deviceName = deviceName;
        this.deviceLocation = deviceLocation;
    }

    public Device() {
    }


    /**
     * Get the ID for the user in the database.
     *
     * @return The ID of the user.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    public Long getId() {
        return id;
    }

    /**
     * Set the ID for this device in the database.
     *
     * @param id The new ID for this device.
     */
    public void setId(Long id) {
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
    @Column(name = "device_location")
    public String getDeviceLocation() {
        return deviceLocation;
    }

    /**
     * Set the string representation of the location of this device.
     *
     * @param deviceLocation The new location of this device.
     */
    public void setDeviceLocation(String deviceLocation) {
        this.deviceLocation = deviceLocation;
    }

    /**
     * Get the unique ID of this device.
     *
     * @return The unique ID of this device.
     */
    @Column(name = "device_uuid")
    public String getDeviceUUID() {
        return deviceUUID;
    }

    /**
     * Set the unique ID of this device.
     *
     * @param deviceUUID THe new unique ID of this device.
     */
    public void setDeviceUUID(String deviceUUID) {
        this.deviceUUID = deviceUUID;
    }

}
