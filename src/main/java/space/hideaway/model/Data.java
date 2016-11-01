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

    @Temporal(value = TemporalType.TIMESTAMP)
    private Date dateTime;
    private Double temperature;

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

    @Column(name = "date")
    public Date getDateTime() {
        return dateTime;
    }

    public void setDateTime(Date dateTime) {
        this.dateTime = dateTime;
    }

    @Column(name = "temperature")
    public Double getTemperature() {
        return temperature;
    }

    public void setTemperature(Double temperature) {
        this.temperature = temperature;
    }
}
