package space.hideaway.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonView;
import org.hibernate.annotations.GenericGenerator;
import org.springframework.data.jpa.datatables.mapping.DataTablesOutput;

import javax.persistence.*;
import java.util.Date;
import java.util.UUID;


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


    public Data() {

    }


    public Data(UUID id, UUID deviceID, Date dateTime, double temperature) {
        this.id = id;
        this.deviceID = deviceID;
        this.dateTime = dateTime;
        this.temperature = temperature;
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


    @Column(name = "device_id")
    public UUID getDeviceID() {
        return deviceID;
    }


    public void setDeviceID(UUID deviceID) {
        this.deviceID = deviceID;
    }


    @ManyToOne()
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
                "Data: [ID: %s Device ID: %s Date: %s Temperature: %s]%n",
                getId().toString(),
                getDeviceID(),
                getDateTime(),
                getTemperature()
        );
    }

    @Column(name = "user_id")
    public int getUserID() {
        return userID;
    }

    public void setUserID(int userID) {
        this.userID = userID;
    }

    @ManyToOne()
    @JoinColumn(name = "user_id", insertable = false, updatable = false)
    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }
}
