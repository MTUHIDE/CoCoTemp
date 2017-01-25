package space.hideaway;

/**
 * Created by dough on 1/24/2017.
 */
public class InfoCardSerializer
{
    Long deviceCount;
    Long recordCount;
    Long uploadCount;

    public Long getDeviceCount()
    {
        return deviceCount;
    }

    public void setDeviceCount(Long deviceCount)
    {
        this.deviceCount = deviceCount;
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
}
