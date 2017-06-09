package space.hideaway.model;

import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.util.Set;
import java.util.UUID;

/**
 * Created by Justin on 6/8/2017.
 */

@Entity
@Table(name = "device")
public class Device {

    private UUID id;
    private Long userID;
    private User user;
    private Long manufacture_num;
    private String type;

    private Set<Site> siteSet;
    private Set<Data> dataSet;
    private Set<UploadHistory> uploadHistories;

    public Device(){

    }

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
    public Long getManufacture_num(){
        return manufacture_num;
    }

    public void setManufacture_num(Long manufacture_num){
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

    @Column(name = "user_id")
    public Long getUserID() {
        return userID;
    }

    public void setUserID(Long userID) {
        this.userID = userID;
    }

    //---------------------------Associations------------------------------------
    //-------------------------------user----------------------------------------

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

    //------------------------------data----------------------------------------

  @OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinColumn(name = "device_id", updatable = false)
    public Set<Data> getDataSet() {
        return dataSet;
    }

    public void setDataSet(Set<Data> dataSet) {
        this.dataSet = dataSet;
    }

    //------------------------------uploadHistories-----------------------------

    @OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinColumn(name = "device_id", updatable = false)
    public Set<UploadHistory> getUploadHistories() {
        return uploadHistories;
    }

    public void setUploadHistories(Set<UploadHistory> uploadHistories) {
        this.uploadHistories = uploadHistories;
    }

    //------------------------------device_site---------------------------------

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(name = "device_site", joinColumns = @JoinColumn(name = "device_id"),
            inverseJoinColumns = @JoinColumn(name = "site_id"))
    public Set<Site> getSiteSet() {
        return siteSet;
    }

    public void setSiteSet(Set<Site> siteSet) {
        this.siteSet = siteSet;
    }
}
