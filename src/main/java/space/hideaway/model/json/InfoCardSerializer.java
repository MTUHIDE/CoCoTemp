package space.hideaway.model.json;

/**
 * Created by dough on 1/24/2017.
 */
public class InfoCardSerializer
{
    Long siteCount;
    Long recordCount;
    Long uploadCount;

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
}
