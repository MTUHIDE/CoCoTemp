package space.hideaway.model;

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
@Table(name = "device_statistics")
public class StationStatistics
{

    public static final StationStatistics EMPTY_STATISTIC = new StationStatistics();

    UUID statisticsID;
    UUID deviceID;

    @JsonIgnore
    Device device;
    double weekMax;
    double weekMin;
    double weekAvg;
    double weekDeviation;
    double monthMax;
    double monthMin;
    double monthAvg;
    double monthDeviation;
    double yearMax;
    double yearMin;
    double yearAvg;
    double yearDeviation;
    double allMax;
    double allMin;
    double allAvg;
    double allDeviation;
    private Date date;

    @GeneratedValue(generator = "uuid2")
    @GenericGenerator(name = "uuid2", strategy = "uuid2")
    @Id
    @Column(name = "statistics_id")
    public UUID getStatisticsID()
    {
        return statisticsID;
    }

    public void setStatisticsID(UUID statisticsID)
    {
        this.statisticsID = statisticsID;
    }

    @Column(name = "device_id")
    public UUID getDeviceID()
    {
        return deviceID;
    }

    public void setDeviceID(UUID deviceID)
    {
        this.deviceID = deviceID;
    }

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "device_id", insertable = false, updatable = false)
    public Device getDevice()
    {
        return device;
    }

    public void setDevice(Device device)
    {
        this.device = device;
    }

    @Column(name = "date")
    public Date getDate()
    {
        return date;
    }

    public void setDate(Date date)
    {
        this.date = date;
    }

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

    public void setWeek(SummaryStatistics week)
    {
        this.weekMin = week.getMin();
        this.weekMax = week.getMax();
        this.weekAvg = week.getMean();
        this.weekDeviation = week.getStandardDeviation();
    }

    public void setMonth(SummaryStatistics month)
    {
        this.monthMin = month.getMin();
        this.monthMax = month.getMax();
        this.monthAvg = month.getMean();
        this.monthDeviation = month.getStandardDeviation();
    }

    public void setYear(SummaryStatistics year)
    {
        this.yearMin = year.getMin();
        this.yearMax = year.getMax();
        this.yearAvg = year.getMean();
        this.yearDeviation = year.getStandardDeviation();
    }

    public void setAll(SummaryStatistics all)
    {
        this.allMin = all.getMin();
        this.allMax = all.getMax();
        this.allAvg = all.getMean();
        this.allDeviation = all.getStandardDeviation();
    }
}
