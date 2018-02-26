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
    private String canopyType;
    private double groundElevation;
    private double floorElevation;
    private double percentEnclosed;
    private double areaBeforeObstacle;
    private boolean isSunken;
    private Time maximumNightTime;
    private Time minimumNightTime;

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

    @Column(name = "canopy_type")
    public String getCanopyType()
    {
        return canopyType;
    }

    public void setCanopyType(String canopyType)
    {
        this.canopyType = canopyType;
    }
}


