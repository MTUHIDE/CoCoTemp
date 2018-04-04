package space.hideaway.model.site;

import com.fasterxml.jackson.annotation.JsonIgnore;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.sql.Time;
import java.util.UUID;

@Entity
@Table(name = "site_metadata")
public class SiteMetadata {

    private UUID metadataId;

    private UUID siteID;
    private Site site;

    private String environment;
    private double groundElevation;
    private double floorElevation;
    private double percentEnclosed;
    private double areaBeforeObstacle;
    private boolean isSunken;
    private Time maximumNightTime;
    private Time minimumNightTime;
    private double slope;

    @GeneratedValue(generator = "uuid2")
    @GenericGenerator(name = "uuid2", strategy = "uuid2")
    @Id
    @Column(name = "metadata_id", length = 16)
    public UUID getMetadataId()
    {
        return metadataId;
    }

    public void setMetadataId(UUID metadataId)
    {
        this.metadataId = metadataId;
    }

    @Column(name = "site_id", length = 16)
    public UUID getSiteID()
    {
        return siteID;
    }

    public void setSiteID(UUID siteID)
    {
        this.siteID = siteID;
    }

    @JsonIgnore
    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "site_id", insertable = false, updatable = false)
    public Site getSite()
    {
        return site;
    }

    public void setSite(Site site)
    {
        this.site = site;
    }

    @Column(name = "environment")
    public String getEnvironment()
    {
        return environment;
    }

    public void setEnvironment(String environment)
    {
        this.environment = environment;
    }

    @Column(name = "ground_elevation")
    public double getGroundElevation()
    {
        return groundElevation;
    }

    public void setGroundElevation(double groundElevation)
    {
        this.groundElevation = groundElevation;
    }

    @Column(name = "floor_elevation")
    public double getFloorElevation()
    {
        return floorElevation;
    }

    public void setFloorElevation(double floorElevation)
    {
        this.floorElevation = floorElevation;
    }

    @Column(name = "is_sunken")
    public boolean getIsSunken() {return  isSunken;}

    public void setIsSunken(boolean depression) {this.isSunken = isSunken;}

    @Column(name = "percent_enclosed")
    public double getPercentEnclosed() {return percentEnclosed;}

    public void setPercentEnclosed(double percentEnclosed) {this.percentEnclosed = percentEnclosed;}

    @Column(name = "site_slope")
    public double getSlope() {return slope;}

    public void setSlope(double slope) {this.slope = slope;}

    @Column(name = "max_night_time")
    private Time getMaximumNightTime() {return maximumNightTime;}

    public void setMaximumNightTime(Time maximumNightTime) {this.maximumNightTime = maximumNightTime;}

    @Column(name = "min_night_time")
    public Time getMinimumNightTime() {return minimumNightTime;}

    public void setMinimumNightTime(Time minimumNightTime) {this.minimumNightTime = minimumNightTime;}

    @Column(name = "area_before_obstacle")
    public double getAreaBeforeObstacle() {return areaBeforeObstacle;}

    public void setAreaBeforeObstacle(double areaBeforeObstacle) {this.areaBeforeObstacle = areaBeforeObstacle;}
}


