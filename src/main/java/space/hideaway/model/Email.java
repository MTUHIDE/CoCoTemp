package space.hideaway.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "email")
public class Email {
    private String subject;
    private String body;

    @Id
    @Column(name = "subject")
    public String getSubject(){
        return this.subject;
    }
    public void setSubject(String s){
        this.subject = s;
    }

    @Column(name = "body")
    public String getBody(){
        return this.body;
    }
    public void setBody(String b){
        this.body = b;
    }
}
