package space.hideaway.services.site;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import space.hideaway.model.site.SiteMetadata;
import space.hideaway.repositories.site.SiteMetadataRepository;

import java.util.List;

@Service
public class SiteMetadataServiceImplementation implements SiteMetadataService {

    private final SiteMetadataRepository siteMetadataRepository;

    @Autowired
    public SiteMetadataServiceImplementation(
            SiteMetadataRepository siteMetadataRepository)
    {
        this.siteMetadataRepository = siteMetadataRepository;
    }

    @Override
    public List<SiteMetadata> getAllSiteMetadata() {
        return siteMetadataRepository.findAll();
    }
}
