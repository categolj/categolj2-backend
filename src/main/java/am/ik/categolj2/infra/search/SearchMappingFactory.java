package am.ik.categolj2.infra.search;

import am.ik.categolj2.core.logger.LogManager;
import org.hibernate.search.annotations.Factory;
import org.hibernate.search.cfg.SearchMapping;
import org.slf4j.Logger;

public class SearchMappingFactory {
    private static Logger logger = LogManager.getLogger();

    @Factory
    public SearchMapping getSearchMapping() {
        SearchMapping mapping = new SearchMapping();

        logger.info("mapping={}", mapping);
        return mapping;
    }
}
