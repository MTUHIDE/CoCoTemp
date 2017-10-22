package space.hideaway.model.site;

import com.fasterxml.jackson.annotation.JsonIgnore;
import org.apache.commons.math3.stat.descriptive.SummaryStatistics;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.util.Date;
import java.util.UUID;

/**
 * Created by dough on 2017-02-18.
 */
@Entity
@Table(name = "site_statistics")
public class SiteStatistics
{

    // In case a site has no calculated statistics.
    public static final SiteStatistics EMPTY_STATISTIC = new SiteStatistics();

    private UUID statisticsID;

    private UUID siteID;
    private Site site;

    private double weekMax;
    private double weekMin;
    private double weekAvg;
    private double weekDeviation;

    private double monthMax;
    private double monthMin;
    private double monthAvg;
    private double monthDeviation;

    private double yearMax;
    private double yearMin;
    private double yearAvg;
    private double yearDeviation;

    private double allMax;
    private double allMin;
    private double allAvg;
    private double allDeviation;

    private Date date;

    /*----------------------------id----------------------------*/

    @GeneratedValue(generator = "uuid2")
    @GenericGenerator(name = "uuid2", strategy = "uuid2")
    @Id
    @Column(name = "statistics_id", length = 16)
    public UUID getStatisticsID()
    {
        return statisticsID;
    }

    public void setStatisticsID(UUID statisticsID)
    {
        this.statisticsID = statisticsID;
    }

    /*----------------------------site----------------------------*/

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
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "site_id", insertable = false, updatable = false)
    public Site getSite()
    {
        return site;
    }

    public void setSite(Site site)
    {
        this.site = site;
    }

    /*----------------------------date----------------------------*/

    @Column(name = "date")
    public Date getDate()
    {
        return date;
    }

    public void setDate(Date date)
    {
        this.date = date;
    }

    /*----------------------------week----------------------------*/

    @Column(name = "week_max")
    public double getWeekMax()
    {
        return weekMax;
    }

    public void setWeekMax(double weekMax)
    {
        this.weekMax = weekMax;
    }

    @Column(name = "week_min")
    public double getWeekMin()
    {
        return weekMin;
    }

    public void setWeekMin(double weekMin)
    {
        this.weekMin = weekMin;
    }

    @Column(name = "week_avg")
    public double getWeekAvg()
    {
        return weekAvg;
    }

    public void setWeekAvg(double weekAvg)
    {
        this.weekAvg = weekAvg;
    }

    @Column(name = "week_deviation")
    public double getWeekDeviation()
    {
        return weekDeviation;
    }

    public void setWeekDeviation(double weekDeviation)
    {
        this.weekDeviation = weekDeviation;
    }

    /*----------------------------month----------------------------*/

    @Column(name = "month_max")
    public double getMonthMax()
    {
        return monthMax;
    }

    public void setMonthMax(double monthMax)
    {
        this.monthMax = monthMax;
    }

    @Column(name = "month_min")
    public double getMonthMin()
    {
        return monthMin;
    }

    public void setMonthMin(double monthMin)
    {
        this.monthMin = monthMin;
    }

    @Column(name = "month_avg")
    public double getMonthAvg()
    {
        return monthAvg;
    }

    public void setMonthAvg(double monthAvg)
    {
        this.monthAvg = monthAvg;
    }

    @Column(name = "month_deviation")
    public double getMonthDeviation()
    {
        return monthDeviation;
    }

    public void setMonthDeviation(double monthDeviation)
    {
        this.monthDeviation = monthDeviation;
    }

    /*----------------------------year----------------------------*/

    @Column(name = "year_max")
    public double getYearMax()
    {
        return yearMax;
    }

    public void setYearMax(double yearMax)
    {
        this.yearMax = yearMax;
    }

    @Column(name = "year_min")
    public double getYearMin()
    {
        return yearMin;
    }

    public void setYearMin(double yearMin)
    {
        this.yearMin = yearMin;
    }

    @Column(name = "year_avg")
    public double getYearAvg()
    {
        return yearAvg;
    }

    public void setYearAvg(double yearAvg)
    {
        this.yearAvg = yearAvg;
    }

    @Column(name = "year_deviation")
    public double getYearDeviation()
    {
        return yearDeviation;
    }

    public void setYearDeviation(double yearDeviation)
    {
        this.yearDeviation = yearDeviation;
    }

    /*----------------------------all----------------------------*/

    @Column(name = "all_max")
    public double getAllMax()
    {
        return allMax;
    }

    public void setAllMax(double allMax)
    {
        this.allMax = allMax;
    }

    @Column(name = "all_min")
    public double getAllMin()
    {
        return allMin;
    }

    public void setAllMin(double allMin)
    {
        this.allMin = allMin;
    }

    @Column(name = "all_avg")
    public double getAllAvg()
    {
        return allAvg;
    }

    public void setAllAvg(double allAvg)
    {
        this.allAvg = allAvg;
    }

    @Column(name = "all_deviation")
    public double getAllDeviation()
    {
        return allDeviation;
    }

    public void setAllDeviation(double allDeviation)
    {
        this.allDeviation = allDeviation;
    }

    /**
     * Sets min, max, mean, and deviation for a week
     *
     * @param week Statistics for the week
     */
    public void setWeek(SummaryStatistics week)
    {
        this.weekMin = week.getMin();
        this.weekMax = week.getMax();
        this.weekAvg = week.getMean();
        this.weekDeviation = week.getStandardDeviation();
    }

    /**
     * Sets min, max, mean, and deviation for a month.
     *
     * @param month Statistics for the month.
     */
    public void setMonth(SummaryStatistics month)
    {
        this.monthMin = month.getMin();
        this.monthMax = month.getMax();
        this.monthAvg = month.getMean();
        this.monthDeviation = month.getStandardDeviation();
    }

    /**
     * Sets min, max, mean, and deviation for a year.
     *
     * @param year Statistics for the year.
     */
    public void setYear(SummaryStatistics year)
    {
        this.yearMin = year.getMin();
        this.yearMax = year.getMax();
        this.yearAvg = year.getMean();
        this.yearDeviation = year.getStandardDeviation();
    }

    /**
     * Sets min, max, mean, and deviation for a all.
     *
     * @param all Statistics for the all.
     */
    public void setAll(SummaryStatistics all)
    {
        this.allMin = all.getMin();
        this.allMax = all.getMax();
        this.allAvg = all.getMean();
        this.allDeviation = all.getStandardDeviation();
    }
}
