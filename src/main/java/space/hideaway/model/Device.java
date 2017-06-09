package space.hideaway.model;

import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
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

    public Device(){

    }



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



    @Column(name = "user_id")
    public Long getUserID() {
        return userID;
    }

    public void setUserID(Long userID) {
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


    @Column(name = "manufacture_num")
    public Long getManufacture_num(){
        return manufacture_num;
    }

    public void setManufacture_num(Long manufacture_num){
        this.manufacture_num = manufacture_num;
    }



    @Column(name = "type")
    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

}
