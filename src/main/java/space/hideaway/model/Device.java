package space.hideaway.model;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.util.Set;
import java.util.UUID;


@Entity
@Table(name = "device")
@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "id")
public class Device {


    private UUID id;


    private Long userId;

    private String deviceName;

    private double deviceLatitude;

    private double deviceLongitude;

    @JsonIgnore
    private Set<Data> dataSet;

    @JsonIgnore
    private Set<UploadHistory> uploadHistories;


    public Device(String deviceName, double deviceLatitude, double deviceLongitude) {
        this.deviceName = deviceName;
        this.deviceLatitude = deviceLatitude;
        this.deviceLongitude = deviceLongitude;
    }


    public Device() {
    }


    @GeneratedValue(generator = "uuid2")
    @GenericGenerator(name = "uuid2", strategy = "uuid2")
    @Id
    @Column(name = "id")
    public UUID getId() {
        return id;
    }


    public void setId(UUID id) {
        this.id = id;
    }


    @Column(name = "user_id")
    public Long getUserId() {
        return userId;
    }


    public void setUserId(Long userId) {
        this.userId = userId;
    }


    @Column(name = "device_name")
    public String getDeviceName() {
        return deviceName;
    }


    public void setDeviceName(String deviceName) {
        this.deviceName = deviceName;
    }


    @Column(name = "device_latitude")
    public double getDeviceLatitude() {
        return deviceLatitude;
    }


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


    @OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinColumn(name = "device_id")
    public Set<Data> getDataSet() {
        return dataSet;
    }


    public void setDataSet(Set<Data> dataSet) {
        this.dataSet = dataSet;
    }

    @OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinColumn(name = "device_id")
    public Set<UploadHistory> getUploadHistories() {
        return uploadHistories;
    }

    public void setUploadHistories(Set<UploadHistory> uploadHistories) {
        this.uploadHistories = uploadHistories;
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
