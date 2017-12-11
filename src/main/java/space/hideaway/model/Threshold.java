package space.hideaway.model;

import space.hideaway.model.site.Site;

import javax.persistence.*;
import java.util.UUID;

@Entity
@Table(name = "threshold")
public class Threshold {

    private long id;
    private long userId;
    private UUID siteId;

    private String thresholdName;
    private double thresholdValue;

    /**
     * Gets id.
     *
     * @return the id
     */
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    public Long getId() {
        return id;
    }

    /**
     * Sets id.
     *
     * @param id the id
     */
    public void setId(Long id)
    {
        this.id = id;
    }

    /**
     * Gets the name of the threshold line. (User can have custom threshold lines on graph)
     *
     * @return the name of the threshold line
     */
    @Column(name = "threshold_name")
    public String getThresholdName()
    {
        return thresholdName;
    }

    /**
     * Sets name of the threshold line. (User can have custom threshold lines on graph)
     *
     * @param thresholdName the name of the threshold line
     */
    public void setThresholdName(String thresholdName)
    {
        this.thresholdName = thresholdName;
    }

    /**
     * Gets the threshold line value. (User can have custom threshold lines on graph)
     *
     * @return the value of the threshold line
     */
    @Column(name = "threshold_value")
    public double getThresholdValue()
    {
        return thresholdValue;
    }

    /**
     * Sets the threshold line value. (User can have custom threshold lines on graph)
     *
     * @param thresholdValue the value to set the threshold line to
     */
    public void setThresholdValue(double thresholdValue)
    {
        this.thresholdValue = thresholdValue;
    }


    @Column(name = "user_id")
    public long getUserId()
    {
        return userId;
    }

    public void setUserId(long userId)
    {
        this.userId = userId;
    }

    @Column(name = "site_id")
    public UUID getSiteId()
    {
        return siteId;
    }

    public void setSiteId(UUID siteId)
    {
        this.siteId = siteId;
    }

}

