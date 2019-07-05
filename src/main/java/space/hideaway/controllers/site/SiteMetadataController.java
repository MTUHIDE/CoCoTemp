package space.hideaway.controllers.site;

import com.querydsl.core.types.dsl.BooleanExpression;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import space.hideaway.model.site.Site;
import space.hideaway.model.site.SiteMetadata;
import space.hideaway.repositories.site.SiteMetadataRepository;
import space.hideaway.repositories.site.SitePredicatesBuilder;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.UUID;


@RestController
public class SiteMetadataController {

    private final SiteMetadataRepository siteMetadataRepository;

    @Autowired
    public SiteMetadataController(
            SiteMetadataRepository siteMetadataRepository) {
        this.siteMetadataRepository = siteMetadataRepository;

    }

    @RequestMapping(value = "/sitefilter.json", method = RequestMethod.POST)
    public
    @ResponseBody
        List<Object[]> getSiteMetadataPoints(@RequestBody String jsonQueryString)
    {
        SitePredicatesBuilder builder = new SitePredicatesBuilder();

        JSONObject jsonObj = new JSONObject(jsonQueryString);

        JSONArray rules = jsonObj.getJSONArray("rules");
        for (int i = 0; i < rules.length(); i++) {
            String id = (String) rules.getJSONObject(i).get("id");
            String operator = (String) rules.getJSONObject(i).get("operator");
            if (rules.getJSONObject(i).get("value") instanceof JSONArray) {
                JSONArray value = (JSONArray) rules.getJSONObject(i).get("value");
                builder.with(id,operator,value);
            } else {
                String value = (String) rules.getJSONObject(i).get("value");
                builder.with(id,operator,value);
            }
        }


        List list = CreateSiteMetaDataList(builder,jsonObj);
        return list;

    }

    public List<SiteMetadata> CreateSiteMetaDataList(SitePredicatesBuilder builder, JSONObject jsonObj){

        List list = new ArrayList();
        String jsonString = jsonObj.get("condition").toString();
        BooleanExpression bool = builder.build(jsonString);
        Iterable<SiteMetadata> metadata = siteMetadataRepository.findAll(bool);
        for (SiteMetadata item : metadata) {
            Object[] pair = new Object[2];
            pair[0] = item.getSite();
            pair[1] = item;
            list.add(pair);
        }
        return list;
    }

    @RequestMapping(value = "/sitemetadata/{siteID}")
    public SiteMetadata showSite(
            Model model,
            @PathVariable(value = "siteID") UUID siteID)
    {
        return siteMetadataRepository.findBySiteID(siteID);
    }

}
