package space.hideaway.model.json;

/**
 * Created by dough on 1/24/2017.
 *
 * Serializer/Wrapper for the info card on the dashboard (aka "My Sites")
 */
public class InfoCardSerializer
{
    private Long siteCount;
    private Long recordCount;
    private Long uploadCount;
    private Long deviceCount;

    public Long getSiteCount()
    {
        return siteCount;
    }

    public void setSiteCount(Long siteCount)
    {
        this.siteCount = siteCount;
    }

    public Long getRecordCount()
    {
        return recordCount;
    }

    public void setRecordCount(Long recordCount)
    {
        this.recordCount = recordCount;
    }

    public Long getUploadCount()
    {
        return uploadCount;
    }

    public void setUploadCount(Long uploadCount)
    {
        this.uploadCount = uploadCount;
    }

    public Long getDeviceCount() {return deviceCount; }

    public void setDeviceCount(Long deviceCount) {this.deviceCount = deviceCount; }
}
