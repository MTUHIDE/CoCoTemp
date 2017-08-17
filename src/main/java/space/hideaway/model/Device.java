package space.hideaway.model;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.util.Set;
import java.util.UUID;


@Entity
@Table(name = "device")
public class Device {

    private UUID id;

    private Long user_id;
    private User user;

    private String manufacture_num;
    private String type;

    private UUID siteID;
    private Site site;

    private Set<Data> dataSet;
    private Set<UploadHistory> uploadHistories;

    //-------------------------------Columns------------------------------------
    //----------------------------------id--------------------------------------

    @GeneratedValue(generator = "uuid2")
    @GenericGenerator(name = "uuid2", strategy = "uuid2")
    @Id
    @Column(name = "id", length = 16)
    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    //--------------------------manufacture_num----------------------------------

    @Column(name = "manufacture_num")
    public String getManufacture_num(){
        return manufacture_num;
    }

    public void setManufacture_num(String manufacture_num){
        this.manufacture_num = manufacture_num;
    }

    //---------------------------------type--------------------------------------

    @Column(name = "type")
    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    //-------------------------------user_id-------------------------------------

    @JsonIgnore
    @Column(name = "user_id")
    public Long getUserID() {
        return user_id;
    }

    public void setUserID(Long user_id) {
        this.user_id = user_id;
    }


    //-------------------------------site_id-------------------------------------

    @Column(name = "site_id")
    public UUID getSiteID() {
        return siteID;
    }

    public void setSiteID(UUID siteID) {
        this.siteID = siteID;
    }

    //---------------------------Associations------------------------------------
    //-------------------------------user----------------------------------------

    @JsonIgnore
    @ManyToOne()
    @JoinColumn(name = "user_id", insertable = false, updatable = false)
    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    //------------------------------data----------------------------------------

    @JsonIgnore
    @OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinColumn(name = "device_id", updatable = false)
    public Set<Data> getDataSet() {
        return dataSet;
    }

    public void setDataSet(Set<Data> dataSet) {
        this.dataSet = dataSet;
    }

    //------------------------------uploadHistories-----------------------------

    @JsonIgnore
    @OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinColumn(name = "device_id", updatable = false)
    public Set<UploadHistory> getUploadHistories() {
        return uploadHistories;
    }

    public void setUploadHistories(Set<UploadHistory> uploadHistories) {
        this.uploadHistories = uploadHistories;
    }

    //------------------------------site---------------------------------

    @JsonIgnore
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "site_id", updatable = false, insertable = false)
    public Site getSite() {
        return site;
    }

    public void setSite(Site site) {
        this.site = site;
    }
}
