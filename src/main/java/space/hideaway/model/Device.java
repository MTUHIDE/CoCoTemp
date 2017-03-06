package space.hideaway.model;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.search.annotations.*;
import org.hibernate.search.annotations.Index;
import org.hibernate.search.bridge.builtin.DoubleBridge;
import org.hibernate.search.spatial.Coordinates;

import javax.persistence.*;
import java.util.List;
import java.util.Set;
import java.util.UUID;


/**
 * The type Device.
 */
@Entity
@Table(name = "device")
@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "id")
@Indexed
public class Device
{

    private UUID id;

    @JsonIgnore
    private Long userID;

    @JsonIgnore
    @IndexedEmbedded
    private User user;

    @Field(index = Index.YES, analyze = Analyze.YES, store = Store.NO)
    private String deviceName;

    @Field(index = Index.YES, analyze = Analyze.YES, store = Store.NO)
    @FieldBridge(impl = DoubleBridge.class)
    private double deviceLatitude;

    @Field(index = Index.YES, analyze = Analyze.YES, store = Store.NO)
    @FieldBridge(impl = DoubleBridge.class)
    private double deviceLongitude;

    @Field(index = Index.YES, analyze = Analyze.YES, store = Store.NO)
    private String deviceDescription;

    @JsonIgnore
    private Set<Data> dataSet;

    @JsonIgnore
    private Set<UploadHistory> uploadHistories;

    @JsonIgnore
    private List<StationStatistics> stationStatisticsList;

    /**
     * Instantiates a new Device.
     *
     * @param deviceName      the device name
     * @param deviceLatitude  the device latitude
     * @param deviceLongitude the device longitude
     */
    public Device(String deviceName, double deviceLatitude, double deviceLongitude)
    {
        this.deviceName = deviceName;
        this.deviceLatitude = deviceLatitude;
        this.deviceLongitude = deviceLongitude;
    }

    /**
     * Instantiates a new Device.
     */
    public Device()
    {
    }

    @OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinColumn(name = "device_id", updatable = false)
    public List<StationStatistics> getStationStatisticsList()
    {
        return stationStatisticsList;
    }

    public void setStationStatisticsList(List<StationStatistics> stationStatisticsList)
    {
        this.stationStatisticsList = stationStatisticsList;
    }

    /**
     * Gets user id.
     *
     * @return the user id
     */
    @Column(name = "user_id")
    public Long getUserID()
    {
        return userID;
    }

    /**
     * Sets user id.
     *
     * @param userID the user id
     */
    public void setUserID(Long userID)
    {
        this.userID = userID;
    }

    @ManyToOne()
    @JoinColumn(name = "user_id", insertable = false, updatable = false)
    public User getUser()
    {
        return user;
    }

    public void setUser(User user)
    {
        this.user = user;
    }

    /**
     * Gets data set.
     *
     * @return the data set
     */
    @OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinColumn(name = "device_id", updatable = false)
    public Set<Data> getDataSet()
    {
        return dataSet;
    }

    /**
     * Sets data set.
     *
     * @param dataSet the data set
     */
    public void setDataSet(Set<Data> dataSet)
    {
        this.dataSet = dataSet;
    }

    /**
     * Gets upload histories.
     *
     * @return the upload histories
     */
    @OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinColumn(name = "device_id", updatable = false)
    public Set<UploadHistory> getUploadHistories()
    {
        return uploadHistories;
    }

    /**
     * Sets upload histories.
     *
     * @param uploadHistories the upload histories
     */
    public void setUploadHistories(Set<UploadHistory> uploadHistories)
    {
        this.uploadHistories = uploadHistories;
    }

    @Override
    public String toString()
    {
        return String.format(
                "Device: [ID: %s Name: %s Location: %s]%n",
                getId(),
                getDeviceName(),
                getDeviceLatitude());
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
     * Gets device name.
     *
     * @return the device name
     */
    @Column(name = "device_name")
    public String getDeviceName()
    {
        return deviceName;
    }

    /**
     * Sets device name.
     *
     * @param deviceName the device name
     */
    public void setDeviceName(String deviceName)
    {
        this.deviceName = deviceName;
    }

    /**
     * Gets device latitude.
     *
     * @return the device latitude
     */
    @Column(name = "device_latitude")
    public double getDeviceLatitude()
    {
        return deviceLatitude;
    }

    /**
     * Sets device latitude.
     *
     * @param deviceLatitude the device latitude
     */
    public void setDeviceLatitude(double deviceLatitude)
    {
        this.deviceLatitude = deviceLatitude;
    }

    @Column(name = "device_description")
    public String getDeviceDescription()
    {
        return deviceDescription;
    }

    public void setDeviceDescription(String deviceDescription)
    {
        this.deviceDescription = deviceDescription;
    }

    @Transient
    @Spatial(spatialMode = SpatialMode.HASH)
    public Coordinates getLocation()
    {
        return new Coordinates()
        {
            @Override
            public Double getLatitude()
            {
                return getDeviceLatitude();
            }

            @Override
            public Double getLongitude()
            {
                return getDeviceLongitude();
            }
        };
    }

    /**
     * Gets device longitude.
     *
     * @return the device longitude
     */
    @Column(name = "device_longitude")
    public double getDeviceLongitude()
    {
        return deviceLongitude;
    }

    /**
     * Sets device longitude.
     *
     * @param deviceLongitude the device longitude
     */
    public void setDeviceLongitude(double deviceLongitude)
    {
        this.deviceLongitude = deviceLongitude;
    }
}
