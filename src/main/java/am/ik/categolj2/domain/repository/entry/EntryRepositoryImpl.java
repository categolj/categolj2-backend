package am.ik.categolj2.domain.repository.entry;

import java.util.List;

import javax.annotation.PostConstruct;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import org.apache.lucene.search.Sort;
import org.apache.lucene.search.SortField;
import org.hibernate.search.jpa.FullTextEntityManager;
import org.hibernate.search.jpa.Search;
import org.hibernate.search.query.dsl.QueryBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.transaction.annotation.Transactional;

import am.ik.categolj2.domain.model.Entry;

public class EntryRepositoryImpl implements EntryRepositoryCustom {
    @PersistenceContext
    EntityManager entityManager;

    private static final Logger logger = LoggerFactory
            .getLogger(EntryRepositoryImpl.class);

    @Override
    @Transactional(readOnly = true)
    public Page<Entry> searchPageByKeyword(String keyword, Pageable pageable) {
        FullTextEntityManager fullTextEntityManager = Search
                .getFullTextEntityManager(entityManager);
        QueryBuilder queryBuilder = fullTextEntityManager.getSearchFactory()
                .buildQueryBuilder().forEntity(Entry.class).get();
        org.apache.lucene.search.Query query = queryBuilder.keyword()
                .onFields("contents", "title").matching(keyword).createQuery();
        org.apache.lucene.search.Sort sort = new Sort(new SortField("lastModifiedDate", SortField.STRING_VAL, true));
        Query jpaQuery = fullTextEntityManager
                .createFullTextQuery(query, Entry.class)
                .setSort(sort)
                .setFirstResult(pageable.getOffset())
                .setMaxResults(pageable.getPageSize());
        int count = fullTextEntityManager
                .createFullTextQuery(query, Entry.class).getResultSize();
        @SuppressWarnings("unchecked")
        List<Entry> content = jpaQuery.getResultList();
        return new PageImpl<>(content, pageable, count);
    }

    @PostConstruct
    public void doIndex() {
        FullTextEntityManager fullTextEntityManager = Search
                .getFullTextEntityManager(entityManager);
        try {
            logger.debug("create index...");
            fullTextEntityManager.createIndexer().startAndWait();
        } catch (InterruptedException e) {
            logger.warn("interupted!", e);
            Thread.currentThread().interrupt();
        }
    }

}
