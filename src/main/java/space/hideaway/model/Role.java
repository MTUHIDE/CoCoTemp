package space.hideaway.model;

import org.springframework.data.annotation.Id;

import javax.persistence.*;
import java.util.Set;

/**
 * Created by dough on 10/9/2016.
 */
@Entity
@Table(name = "role")
public class Role {
    private Long id;
    private String name;
    private Set<User> userSet;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @ManyToMany(mappedBy = "roles")
    public Set<User> getUserSet() {
        return userSet;
    }

    public void setUserSet(Set<User> userSet) {
        this.userSet = userSet;
    }
}
