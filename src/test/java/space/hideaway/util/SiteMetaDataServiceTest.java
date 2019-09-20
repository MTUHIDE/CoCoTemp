package space.hideaway.util;
import org.junit.*;
import space.hideaway.model.site.SiteMetadata;
import space.hideaway.repositories.site.SiteMetadataRepository;
import space.hideaway.services.site.SiteMetadataService;
import space.hideaway.services.site.SiteMetadataServiceImplementation;

import java.util.ArrayList;
import java.util.List;

import static org.mockito.Mockito.*;

public class SiteMetaDataServiceTest {

    SiteMetadataService siteMetadataService ;
    SiteMetadataRepository mockSiteMetadataRepository;

    @Before
    public void  setUp(){
        mockSiteMetadataRepository = mock(SiteMetadataRepository.class);
        siteMetadataService = new SiteMetadataServiceImplementation(mockSiteMetadataRepository);
    }

    @Test
    public void testgetAllSiteMetadata(){
        SiteMetadata mockSiteMetadata = mock(SiteMetadata.class);
        List<SiteMetadata> siteMetadataList = new ArrayList<>();
        siteMetadataList.add(mockSiteMetadata);
        when(mockSiteMetadataRepository.findAll()).thenReturn(siteMetadataList);
        List<SiteMetadata> actualList = siteMetadataService.getAllSiteMetadata();
        Assert.assertEquals(siteMetadataList, actualList);
        verify(mockSiteMetadataRepository,times(1)).findAll();
    }


    @Test
    public void testSave(){
        SiteMetadata mockSiteMetadata = mock(SiteMetadata.class);
        when(mockSiteMetadataRepository.save(mockSiteMetadata)).thenReturn(mockSiteMetadata);
        SiteMetadata actual = siteMetadataService.save(mockSiteMetadata);
        Assert.assertEquals(mockSiteMetadata,actual);
    }

}
