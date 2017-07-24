package space.hideaway.model;

import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.util.Date;
import java.util.UUID;

/**
 * Created by jmh62 on 7/19/2017.
 */
@Entity
@Table(name = "news")
public class News {

    private long id;

    private Date dateTime;

    private String title;

    private String content;

    /**
     * Gets id.
     *
     * @return the id
     */
    @GenericGenerator(name = "increment", strategy = "increment")
    @GeneratedValue(generator = "increment")
    @Id
    @Column(name = "id", length = 16)
    public long getId()
    {
        return id;
    }

    /**
     * Sets id.
     *
     * @param id the id
     */
    public void setId(long id)
    {
        this.id = id;
    }

    /**
     * Gets date time.
     *
     * @return the date time
     */

    @Temporal(value = TemporalType.TIMESTAMP)
    @Column(name = "date")
    public Date getDateTime()
    {
        return dateTime;
    }

    /**
     * Sets date time.
     *
     * @param dateTime the date time
     */
    public void setDateTime(Date dateTime)
    {
        this.dateTime = dateTime;
    }

    @Column(name = "title")
    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    @Column(name = "content", length = Short.MAX_VALUE)
    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

}
