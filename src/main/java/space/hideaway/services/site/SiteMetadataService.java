package space.hideaway.services.site;

import org.springframework.stereotype.Service;
import space.hideaway.model.site.SiteMetadata;

import java.util.List;

public interface SiteMetadataService
{
    List<SiteMetadata> getAllSiteMetadata();

    SiteMetadata save(SiteMetadata siteMetadata);
}
