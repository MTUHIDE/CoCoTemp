package space.hideaway.model.site;

import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.UUID;

@Entity
@Table(name = "site_metadata")
public class SiteMetadata {

    private UUID metadataId;

    private UUID siteID;
    private Site site;

    private String environment;
    private String nearestWater;
    private int waterDistance;
    private int waterDirection;
    private String maxNightTime;
    private String minNightTime;
    private String purpose;
    private int heightAboveGround;
    private int heightAboveFloor;
    private double elevation;
    private int enclosurePercentage;
    private int nearestAirflowObstacle;
    private int nearestObstacleDegrees;
    private String obstacleType;
    private int areaAroundSensor;
    private boolean riparianArea;
    private String canopyType;
    private double slope;
    private int slopeDirection;
    private double skyViewFactor;

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

    @OneToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "site_id", insertable = false, updatable = false)
    public Site getSite()
    {
        return site;
    }
    public void setSite(Site site)
    {
        this.site = site;
    }

    @Column(name = "obstacle_type")
    public String getObstacleType() { return obstacleType; }
    public void setObstacleType(String obstacleType) { this.obstacleType = obstacleType; }

    @Column(name = "max_night_time")
    public String getMaxNightTime() { return maxNightTime; }
    public void setMaxNightTime(String maxNightTime) { this.maxNightTime = maxNightTime; }

    @Column(name = "min_night_time")
    public String getMinNightTime() { return minNightTime; }
    public void setMinNightTime(String minNightTime) { this.minNightTime = minNightTime; }

    @Column(name = "environment")
    public String getEnvironment() {
        return environment;
    }
    public void setEnvironment(String environment) {
        this.environment = environment;
    }

    @Column(name = "purpose")
    public String getPurpose() {
        return purpose;
    }
    public void setPurpose(String purpose) {
        this.purpose = purpose;
    }

    @Column(name = "height_above_ground")
    public int getHeightAboveGround() {
        return heightAboveGround;
    }
    public void setHeightAboveGround(int heightAboveGround) {
        this.heightAboveGround = heightAboveGround;
    }

    @Column(name = "height_above_floor")
    public int getHeightAboveFloor() {
        return heightAboveFloor;
    }
    public void setHeightAboveFloor(int heightAboveFloor) {
        this.heightAboveFloor = heightAboveFloor;
    }

    @Column(name = "enclosure_percentage")
    public int getEnclosurePercentage() {
        return enclosurePercentage;
    }
    public void setEnclosurePercentage(int enclosurePercentage) {
        this.enclosurePercentage = enclosurePercentage;
    }

    @Column(name = "nearest_airflow_obstacle")
    public int getNearestAirflowObstacle() {
        return nearestAirflowObstacle;
    }
    public void setNearestAirflowObstacle(int nearestAirflowObstacle) { this.nearestAirflowObstacle = nearestAirflowObstacle; }

    @Column(name = "nearest_obstacle_degrees")
    public int getNearestObstacleDegrees() {
        return nearestObstacleDegrees;
    }
    public void setNearestObstacleDegrees(int nearestObstacleDegrees) { this.nearestObstacleDegrees = nearestObstacleDegrees; }

    @Column(name = "area_around_sensor")
    public int getAreaAroundSensor() {
        return areaAroundSensor;
    }
    public void setAreaAroundSensor(int areaAroundSensor) {
        this.areaAroundSensor = areaAroundSensor;
    }

    @Column(name = "riparian_area")
    public boolean isRiparianArea() {
        return riparianArea;
    }
    public void setRiparianArea(boolean riparianArea) {
        this.riparianArea = riparianArea;
    }

    @Column(name = "canopy_type")
    public String getCanopyType() {
        return canopyType;
    }
    public void setCanopyType(String canopyType) {
        this.canopyType = canopyType;
    }

    @Column(name = "slope_direction")
    public int getSlopeDirection() { return slopeDirection; }
    public void setSlopeDirection(int slopeDirection) { this.slopeDirection = slopeDirection; }

    @Column(name = "nearest_water")
    public String getNearestWater() { return nearestWater; }
    public void setNearestWater(String nearestWater) { this.nearestWater = nearestWater; }

    @Column(name = "water_distance")
    public int getWaterDistance() { return waterDistance; }
    public void setWaterDistance(int waterDistance) { this.waterDistance= waterDistance; }

    @Column(name = "water_direction")
    public int getWaterDirection() { return waterDirection; }
    public void setWaterDirection(int waterDirection) { this.waterDirection = waterDirection; }

    @Column(name = "slope")
    public double getSlope() {
        return slope;
    }
    public void setSlope(double slope) {
        this.slope = slope;
    }

    @Column(name = "sky_view_factor")
    public double getSkyViewFactor() {
        return skyViewFactor;
    }
    public void setSkyViewFactor(double skyViewFactor) {
        this.skyViewFactor = skyViewFactor;
    }

    @Column(name = "elevation")
    public double getElevation() {
        return elevation;
    }
    public void setElevation(double elevation) {
        this.elevation = elevation;
    }
}


