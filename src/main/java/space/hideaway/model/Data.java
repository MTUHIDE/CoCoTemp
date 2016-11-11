package space.hideaway.model;

import com.fasterxml.jackson.annotation.JsonView;
import org.csveed.annotations.CsvCell;
import org.csveed.annotations.CsvDate;
import org.csveed.annotations.CsvFile;
import org.csveed.annotations.CsvIgnore;
import org.hibernate.annotations.GenericGenerator;
import org.springframework.data.jpa.datatables.mapping.DataTablesOutput;

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
    @JsonView(DataTablesOutput.View.class)
    private Date dateTime;

    @JsonView(DataTablesOutput.View.class)
    @CsvCell(columnName = "temperature", required = true)
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
