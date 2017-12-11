package space.hideaway.model.site;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.search.annotations.*;
import org.hibernate.search.annotations.Index;
import org.hibernate.search.bridge.builtin.DoubleBridge;
import org.hibernate.search.spatial.Coordinates;
import space.hideaway.model.Data;
import space.hideaway.model.Device;
import space.hideaway.model.Threshold;
import space.hideaway.model.User;
import space.hideaway.model.globe.Globe;
import space.hideaway.model.upload.UploadHistory;

import javax.persistence.*;
import java.util.List;
import java.util.Set;
import java.util.UUID;


/**
 * The table Site.
 */
@Entity
@Table(name = "site")
@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "id")
@Indexed
public class Site
{

    private UUID id;

    private Long userID;
    private User user;

    private String siteName;
    private double siteLatitude;
    private double siteLongitude;
    private String siteDescription;
    private double siteElevation;

    private List<Globe> globeSet;
    private Set<Device> deviceSet;
    private Set<Data> dataSet;

    private Set<UploadHistory> uploadHistories;
    private Set<Threshold> thresholdSet;

    private List<SiteStatistics> siteStatisticsList;

    /**
     * Instantiates a new Site.
     *
     * @param siteName      the site name
     * @param siteLatitude  the site latitude
     * @param siteLongitude the site longitude
     */
    public Site(String siteName, double siteLatitude, double siteLongitude)
    {
        this.siteName = siteName;
        this.siteLatitude = siteLatitude;
        this.siteLongitude = siteLongitude;
    }

    /**
     * Instantiates a new Site.
     */
    public Site()
    {
    }

    /**
     * Gets site statistics.
     *
     * @return the site statistics.
     */
    @JsonIgnore
    @OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinColumn(name = "site_id", updatable = false)
    public List<SiteStatistics> getSiteStatisticsList()
    {
        return siteStatisticsList;
    }

    /**
     * Sets site statistics.
     *
     * @param siteStatisticsList the site statistics.
     */
    public void setSiteStatisticsList(List<SiteStatistics> siteStatisticsList)
    {
        this.siteStatisticsList = siteStatisticsList;
    }

    /**
     * Gets user id.
     *
     * @return the user id
     */
    @Column(name = "user_id")
    @JsonIgnore
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

    @JsonIgnore
    @IndexedEmbedded
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
    @JsonIgnore
    @OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinColumn(name = "site_id", updatable = false)
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
     * Gets globe set.
     *
     * @return the globe set
     */
    @JsonIgnore
    @OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinColumn(name = "site_id", updatable = false)
    public List<Globe> getGlobeSet()
    {
        return globeSet;
    }

    /**
     * Sets globe set.
     *
     * @param globeSet the data set
     */
    public void setGlobeSet(List<Globe> globeSet)
    {
        this.globeSet = globeSet;
    }

    /**
     * Gets upload histories.
     *
     * @return the upload histories
     */
    @JsonIgnore
    @OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinColumn(name = "site_id", updatable = false)
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

    /**
     * Gets id.
     *
     * @return the id
     */
    @GeneratedValue(generator = "uuid2")
    @GenericGenerator(name = "uuid2", strategy = "uuid2")
    @Id
    @Column(name = "id", length = 16)
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
     * Gets site name.
     *
     * @return the site name
     */
    @Field(index = Index.YES, analyze = Analyze.YES, store = Store.NO)
    @Column(name = "site_name")
    public String getSiteName()
    {
        return siteName;
    }

    /**
     * Sets site name.
     *
     * @param siteName the site name
     */
    public void setSiteName(String siteName)
    {
        this.siteName = siteName;
    }

    /**
     * Gets site latitude.
     *
     * @return the site latitude
     */
    @Field(index = Index.YES, analyze = Analyze.YES, store = Store.NO)
    @FieldBridge(impl = DoubleBridge.class)
    @Column(name = "site_latitude")
    public double getSiteLatitude()
    {
        return siteLatitude;
    }

    /**
     * Sets site latitude.
     *
     * @param siteLatitude the site latitude
     */
    public void setSiteLatitude(double siteLatitude)
    {
        this.siteLatitude = siteLatitude;
    }

    /**
     * Gets the site description.
     *
     * @return the site description.
     */
    @Field(index = Index.YES, analyze = Analyze.YES, store = Store.NO)
    @Column(name = "site_description")
    public String getSiteDescription()
    {
        return siteDescription;
    }

    /**
     * Sets the site description.
     *
     * @param siteDescription the site description.
     */
    public void setSiteDescription(String siteDescription)
    {
        this.siteDescription = siteDescription;
    }

    /**
     * Gets site longitude.
     *
     * @return the site longitude
     */
    @Field(index = Index.YES, analyze = Analyze.YES, store = Store.NO)
    @FieldBridge(impl = DoubleBridge.class)
    @Column(name = "site_longitude")
    public double getSiteLongitude()
    {
        return siteLongitude;
    }

    /**
     * Sets site longitude.
     *
     * @param siteLongitude the site longitude
     */
    public void setSiteLongitude(double siteLongitude)
    {
        this.siteLongitude = siteLongitude;
    }

    /**
     * Gets site elevation.
     *
     * @return the site elevation
     */
    @Column(name = "site_elevation")
    public double getSiteElevation()
    {
        return siteElevation;
    }

    /**
     * Sets site elevation.
     *
     * @param siteElevation the site elevation
     */
    public void setSiteElevation(double siteElevation)
    {
        this.siteElevation = siteElevation;
    }

    /**
     * Gets the site device set.
     *
     * @return the site device set.
     */
    @OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinColumn(name = "site_id", updatable = false)
    public Set<Device> getDeviceSet()
    {
        return deviceSet;
    }

    /**
     * Sets the site device set.
     *
     * @param deviceSet the site device set.
     */
    public void setDeviceSet(Set<Device> deviceSet)
    {
        this.deviceSet = deviceSet;
    }

    /**
     * Formats the site's location into a coordinate.
     *
     * @return A coordinate
     */
    @Transient
    @JsonIgnore
    @Spatial(spatialMode = SpatialMode.HASH)
    public Coordinates getLocation()
    {
        return new Coordinates()
        {
            @Override
            public Double getLatitude()
            {
                return getSiteLatitude();
            }

            @Override
            public Double getLongitude()
            {
                return getSiteLongitude();
            }
        };
    }

    /**
     * Site: [ID: 12354034804 Name: Demo Site Location: 25, 56]
     *
     * @return A String.
     */
    @Override
    public String toString()
    {
        return String.format(
                "Site: [ID: %s Name: %s Location: %s, %s]%n",
                getId(),
                getSiteName(),
                getSiteLatitude(),
                getSiteLongitude());
    }

    /**
     * Gets the threshold set
     *
     * @return the threshold lines
     */
    @OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinColumn(name = "site_id")
    public Set<Threshold> getThresholdSet() {
        return thresholdSet;
    }

    /**
     * Sets threshold set
     *
     * @param thresholds a set of user defined threshold lines
     */
    public void setThresholdSet(Set<Threshold> thresholds) {
        this.thresholdSet = thresholds;
    }

}
