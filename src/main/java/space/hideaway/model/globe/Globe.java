package space.hideaway.model.globe;

import org.hibernate.annotations.GenericGenerator;
import space.hideaway.model.Site;

import javax.persistence.*;
import java.util.UUID;

@Entity
@Table(name = "globe")
public class Globe {

    private UUID id;

    private UUID siteID;
    private Site site;

    private byte question_number;

    private String answer;

    @GeneratedValue(generator = "uuid2")
    @GenericGenerator(name = "uuid2", strategy = "uuid2")
    @Id
    @Column(name = "id", length = 16)
    public UUID getId()
    {
        return id;
    }

    public void setId(UUID id)
    {
        this.id = id;
    }

    @Column(name = "site_id", length = 16)
    public UUID getSiteID() {
        return siteID;
    }

    public void setSiteID(UUID siteID) {
        this.siteID = siteID;
    }

    @ManyToOne()
    @JoinColumn(name = "site_id", updatable = false, insertable = false)
    public Site getSite() {
        return site;
    }

    public void setSite(Site site) {
        this.site = site;
    }

    @Column(name = "answer")
    public String getAnswer() {
        return answer;
    }

    public void setAnswer(String answer) {
        this.answer = answer;
    }

    @Column(name = "question_number")
    public byte getQuestion_number() {
        return question_number;
    }

    public void setQuestion_number(byte question_number) {
        this.question_number = question_number;
    }


}
