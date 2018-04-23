package space.hideaway.model.site;

import com.fasterxml.jackson.annotation.JsonIgnore;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.sql.Time;
import java.util.ArrayList;
import java.util.UUID;

@Entity
@Table(name = "site_metadata")
public class SiteMetadata {

    private UUID metadataId;

    private UUID siteID;
    private Site site;

    private String environment;
//    private double groundElevation;
//    private double floorElevation;
//    private double percentEnclosed;
//    private double areaBeforeObstacle;
//    private double obstacleDirection;
//    private String obstacleType;
//    private boolean isSunken;
//    private Time maximumNightTime;
//    private Time minimumNightTime;
//    private double slope;
//    private String canopyType;
//    private int slopeDirection;
//    private String nearestWater;
//    private int waterDistance;
//    private int waterDirection;
//
//    private ArrayList<String> purpose;
//    private ArrayList<String> maxNightTime;
//    private ArrayList<String> minNightTime;

    private String purpose;
    private int heightAboveGround;
    private int heightAboveFloor;
    private int enclosurePercentage;
    private int nearestAirflowObstacle;
    private int nearestObstacleDegrees;
    private int areaAroundSensor;
    private boolean riparianArea;
    private String canopyType;
    private int distanceToWater;
    private int slope;
    private int skyViewFactor;

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

    @Column(name = "environment")
//<<<<<<< HEAD
//    public String getEnvironment()
//    {
//        return environment;
//    }
//
//    public void setEnvironment(String environment)
//    {
//        this.environment = environment;
//    }
//
//    @Column(name = "ground_elevation")
//    public double getGroundElevation()
//    {
//        return groundElevation;
//    }
//
//    public void setGroundElevation(double groundElevation)
//    {
//        this.groundElevation = groundElevation;
//    }
//
//    @Column(name = "floor_elevation")
//    public double getFloorElevation()
//    {
//        return floorElevation;
//    }
//
//    public void setFloorElevation(double floorElevation)
//    {
//        this.floorElevation = floorElevation;
//    }
//
//    @Column(name = "obstacle_direction")
//    public double getObstacleDirection()
//    {
//        return obstacleDirection;
//    }
//
//    public void setObstacleDirection(double obstacleDirection)
//    {
//        this.obstacleDirection = obstacleDirection;
//    }
//
//    @Column(name = "obstacle_type")
//    public String getObstacleType()
//    {
//        return obstacleType;
//    }
//
//    public void setObstacleType(String obstacleType)
//    {
//        this.obstacleType = obstacleType;
//    }
//
//    @Column(name = "is_sunken")
//    public boolean getIsSunken() {return  isSunken;}
//
//    public void setIsSunken(boolean depression) {this.isSunken = isSunken;}
//
//    @Column(name = "percent_enclosed")
//    public double getPercentEnclosed() {return percentEnclosed;}
//
//    public void setPercentEnclosed(double percentEnclosed) {this.percentEnclosed = percentEnclosed;}
//
//    @Column(name = "site_slope")
//    public double getSlope() {return slope;}
//
//    public void setSlope(double slope) {this.slope = slope;}
//
//    @Column(name = "max_night_time")
//    private Time getMaximumNightTime() {return maximumNightTime;}
//
//    public void setMaximumNightTime(Time maximumNightTime) {this.maximumNightTime = maximumNightTime;}
//
//    @Column(name = "min_night_time")
//    public Time getMinimumNightTime() {return minimumNightTime;}
//
//    public void setMinimumNightTime(Time minimumNightTime) {this.minimumNightTime = minimumNightTime;}
//
//    @Column(name = "area_before_obstacle")
//    public double getAreaBeforeObstacle() {return areaBeforeObstacle;}
//
//    public void setAreaBeforeObstacle(double areaBeforeObstacle) {this.areaBeforeObstacle = areaBeforeObstacle;}
//
//    @Column(name = "purpose")
//    public ArrayList<String> getPurpose() {
//        return purpose;
//    }
//
//    public void setPurpose(ArrayList<String> purpose) {
//        this.purpose = purpose;
//    }
//
//    public ArrayList<String> getMaxNightTime() {
//        return maxNightTime;
//    }
//
//    public void setMaxNightTime(ArrayList<String> maxNightTime) {
//        this.maxNightTime = maxNightTime;
//    }
//
//    public ArrayList<String> getMinNightTime() {
//        return minNightTime;
//    }
//
//    public void setMinNightTime(ArrayList<String> minNightTime) {
//        this.minNightTime = minNightTime;
//    }
//
//=======
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

    public void setNearestAirflowObstacle(int nearestAirflowObstacle) {
        this.nearestAirflowObstacle = nearestAirflowObstacle;
    }

    @Column(name = "nearest_obstacle_degrees")
    public int getNearestObstacleDegrees() {
        return nearestObstacleDegrees;
    }

    public void setNearestObstacleDegrees(int nearestObstacleDegrees) {
        this.nearestObstacleDegrees = nearestObstacleDegrees;
    }

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

//<<<<<<< HEAD
//    public int getSlopeDirection() {
//        return slopeDirection;
//    }
//
//    public void setSlopeDirection(int slopeDirection) {
//        this.slopeDirection = slopeDirection;
//    }
//
//    public String getNearestWater() {
//        return nearestWater;
//    }
//
//    public void setNearestWater(String nearestWater) {
//        this.nearestWater = nearestWater;
//    }
//
//    public int getWaterDistance() {
//        return waterDistance;
//    }
//
//    public void setWaterDistance(int waterDistance) {
//        this.waterDistance= waterDistance;
//    }
//
//    public int getWaterDirection() {
//        return waterDirection;
//    }
//
//    public void setWaterDirection(int waterDirection) {
//        this.waterDirection = waterDirection;
//=======
    @Column(name = "distance_to_water")
    public int getDistanceToWater() {
        return distanceToWater;
    }

    public void setDistanceToWater(int distanceToWater) {
        this.distanceToWater = distanceToWater;
    }

    @Column(name = "slope")
    public int getSlope() {
        return slope;
    }

    public void setSlope(int slope) {
        this.slope = slope;
    }

    @Column(name = "sky_view_factor")
    public int getSkyViewFactor() {
        return skyViewFactor;
    }

    public void setSkyViewFactor(int skyViewFactor) {
        this.skyViewFactor = skyViewFactor;
    }
}


