package space.hideaway.model;

import javax.persistence.*;
import java.util.Date;

/**
 * Created by dough on 11/1/2016.
 */
@Entity
@Table(name = "data")
public class Data {
    private Long id;
    private Long deviceID;
    private Device device;
    @Temporal(value = TemporalType.TIMESTAMP)
    private Date dateTime;
    private double temperature;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    @Column(name = "id")
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    @Column(name = "device_id")
    public Long getDeviceID() {
        return deviceID;
    }

    public void setDeviceID(Long deviceID) {
        this.deviceID = deviceID;
    }

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "device_id", insertable = false, updatable = false)
    public Device getDevice() {
        return device;
    }

    public void setDevice(Device device) {
        this.device = device;
    }

    @Column(name = "date")
    public Date getDateTime() {
        return dateTime;
    }

    public void setDateTime(Date dateTime) {
        this.dateTime = dateTime;
    }

    @Column(name = "temperature")
    public double getTemperature() {
        return temperature;
    }

    public void setTemperature(double temperature) {
        this.temperature = temperature;
    }

    @Override
    public String toString() {
        return String.format(
                "Data: [ID: %d Device ID: %d Date: %s Temperature: %s]%n",
                getId(),
                getDeviceID(),
                getDateTime(),
                getTemperature()
        );
    }
}
