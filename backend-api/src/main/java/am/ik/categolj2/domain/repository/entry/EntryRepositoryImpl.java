/*
 * Copyright (C) 2014 Toshiaki Maki
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND,
 * either express or implied. See the License for the specific language
 * governing permissions and limitations under the License.
 */
package am.ik.categolj2.domain.repository.entry;

import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
import java.util.function.Function;

import javax.annotation.PostConstruct;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import am.ik.categolj2.core.logger.LogManager;
import org.apache.lucene.search.Sort;
import org.apache.lucene.search.SortField;
import org.hibernate.search.jpa.FullTextEntityManager;
import org.hibernate.search.jpa.Search;
import org.hibernate.search.query.dsl.QueryBuilder;
import org.slf4j.Logger;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.transaction.annotation.Transactional;

import am.ik.categolj2.domain.model.Entry;

public class EntryRepositoryImpl implements EntryRepositoryCustom {
    @PersistenceContext
    EntityManager entityManager;
    Future<?> creatingIndex;

    private static final Logger logger = LogManager.getLogger();

    Page<Entry> searchTemplate(Pageable pageable, Function<QueryBuilder, org.apache.lucene.search.Query> queryCreator) {
        try {
            creatingIndex.get();
        } catch (InterruptedException e) {
            logger.warn("Interrupted!", e);
            Thread.currentThread().interrupt();
        } catch (ExecutionException e) {
            logger.error("Index creation failed!!", e);
        }
        FullTextEntityManager fullTextEntityManager = Search
                .getFullTextEntityManager(entityManager);
        QueryBuilder queryBuilder = fullTextEntityManager.getSearchFactory()
                .buildQueryBuilder().forEntity(Entry.class).get();

        org.apache.lucene.search.Query query = queryCreator.apply(queryBuilder);
        org.apache.lucene.search.Sort sort = new Sort(new SortField("lastModifiedDate", SortField.Type.STRING_VAL, true));
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

    org.apache.lucene.search.Query createSearchQuery(String keyword, QueryBuilder queryBuilder) {
        return queryBuilder.keyword()
                .onFields("contents", "title").matching(keyword).createQuery();

    }

    @Override
    @Transactional(readOnly = true)
    public Page<Entry> searchPageByKeyword(String keyword, Pageable pageable) {
        return searchTemplate(pageable, queryBuilder ->
                createSearchQuery(keyword, queryBuilder));
    }

    @Override
    @Transactional(readOnly = true)
    public Page<Entry> searchPagePublishedByKeyword(String keyword, Pageable pageable) {
        return searchTemplate(pageable, queryBuilder ->
                queryBuilder.bool()
                        .must(createSearchQuery(keyword, queryBuilder))
                        .must(queryBuilder.keyword().onField("published").matching(true).createQuery())
                        .createQuery()
        );
    }

    @PostConstruct
    public void doIndex() {
        FullTextEntityManager fullTextEntityManager = Search
                .getFullTextEntityManager(entityManager);
        logger.info("Create index...");
        creatingIndex = fullTextEntityManager.createIndexer().start();
    }

}
