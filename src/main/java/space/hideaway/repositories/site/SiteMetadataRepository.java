package space.hideaway.repositories.site;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import com.querydsl.core.types.dsl.StringExpression;
import com.querydsl.core.types.dsl.StringPath;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.querydsl.QueryDslPredicateExecutor;
import org.springframework.data.querydsl.binding.QuerydslBinderCustomizer;
import org.springframework.data.querydsl.binding.QuerydslBindings;
import org.springframework.data.querydsl.binding.SingleValueBinding;
import org.springframework.stereotype.Repository;
import space.hideaway.model.site.QSiteMetadata;
import space.hideaway.model.site.Site;
import space.hideaway.model.site.SiteMetadata;

import java.util.UUID;

@Repository
public interface SiteMetadataRepository extends JpaRepository<SiteMetadata, UUID>,
        QueryDslPredicateExecutor<SiteMetadata>, QuerydslBinderCustomizer<QSiteMetadata> {

    default public void customize(
            QuerydslBindings bindings, QSiteMetadata root) {
        bindings.bind(String.class)
                .first((SingleValueBinding<StringPath, String>) StringExpression::containsIgnoreCase);

    }

    SiteMetadata findBySiteID(UUID siteID);
}
