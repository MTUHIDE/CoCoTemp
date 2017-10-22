package space.hideaway.model.upload;

import space.hideaway.model.site.Site;

/**
 * Created by dough on 2017-02-18.
 */
public class UploadFormWrapper
{
    private Site selectedSite;
    private String description;

    public Site getSelectedSite()
    {
        return selectedSite;
    }

    public void setSelectedSite(Site selectedSite)
    {
        this.selectedSite = selectedSite;
    }

    public String getDescription()
    {
        return description;
    }

    public void setDescription(String description)
    {
        this.description = description;
    }
}
