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
 * The type Site.
 */
@Entity
@Table(name = "site")
@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "id")
@Indexed
public class Site
{

    private UUID id;

    @JsonIgnore
    private Long userID;

    @JsonIgnore
    @IndexedEmbedded
    private User user;

    private Globe globe;
    private UUID globe_id;

    @Field(index = Index.YES, analyze = Analyze.YES, store = Store.NO)
    private String siteName;

    @Field(index = Index.YES, analyze = Analyze.YES, store = Store.NO)
    @FieldBridge(impl = DoubleBridge.class)
    private double siteLatitude;

    @Field(index = Index.YES, analyze = Analyze.YES, store = Store.NO)
    @FieldBridge(impl = DoubleBridge.class)
    private double siteLongitude;

    @Field(index = Index.YES, analyze = Analyze.YES, store = Store.NO)
    private String siteDescription;

    @JsonIgnore
    private Set<Data> dataSet;

    @JsonIgnore
    private Set<UploadHistory> uploadHistories;

    @JsonIgnore
    private List<SiteStatistics> siteStatisticsList;

    private Set<Device> deviceSet;

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

    @OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinColumn(name = "site_id", updatable = false)
    public List<SiteStatistics> getSiteStatisticsList()
    {
        return siteStatisticsList;
    }

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




    @Column(name = "globe_id")
    public UUID getGlobeID()
    {
        return globe_id;
    }

    public void setGlobeID(UUID globe_id)
    {
        this.globe_id = globe_id;
    }

    @OneToOne()
    @JoinColumn(name = "globe_id", insertable = false, updatable = false)
    public Globe getGlobe()
    {
        return globe;
    }

    public void setGlobe(Globe globe)
    {
        this.globe = globe;
    }





    /**
     * Gets data set.
     *
     * @return the data set
     */
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
     * Gets upload histories.
     *
     * @return the upload histories
     */
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

    @Override
    public String toString()
    {
        return String.format(
                "Site: [ID: %s Name: %s Location: %s]%n",
                getId(),
                getSiteName(),
                getSiteLatitude());
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

    @Column(name = "site_description")
    public String getSiteDescription()
    {
        return siteDescription;
    }

    public void setSiteDescription(String siteDescription)
    {
        this.siteDescription = siteDescription;
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
     * Gets site longitude.
     *
     * @return the site longitude
     */
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

    @OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinColumn(name = "site_id", updatable = false)
    public Set<Device> getDeviceSet() {
        return deviceSet;
    }

    public void setDeviceSet(Set<Device> deviceSet) {
        this.deviceSet = deviceSet;
    }

}
