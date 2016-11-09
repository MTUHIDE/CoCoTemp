package space.hideaway.model;

import org.csveed.annotations.CsvCell;
import org.csveed.annotations.CsvDate;
import org.csveed.annotations.CsvFile;
import org.csveed.annotations.CsvIgnore;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.util.Date;
import java.util.UUID;

/**
 * Created by dough on 11/1/2016.
 */
@Entity
@Table(name = "data")
@CsvFile(separator = ',')
public class Data {
    @CsvIgnore
    private UUID id;

    @CsvIgnore
    private UUID deviceID;

    @CsvIgnore
    private Device device;


    @Temporal(value = TemporalType.TIMESTAMP)
    @CsvCell(columnName = "dateTime", required = true)
    @CsvDate(format = "yyyy-MM-dd HH:mm:ss")
    private Date dateTime;

    @CsvCell(columnName = "temperature", required = true)
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
                "Data: [ID: %s Device ID: %s Date: %s Temperature: %s]%n",
                getId().toString(),
                getDeviceID(),
                getDateTime(),
                getTemperature()
        );
    }

}
