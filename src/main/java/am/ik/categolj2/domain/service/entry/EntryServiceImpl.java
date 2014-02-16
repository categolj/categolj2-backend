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
package am.ik.categolj2.domain.service.entry;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import am.ik.categolj2.core.logger.LogManager;
import com.google.common.collect.Multimap;
import com.google.common.collect.TreeMultimap;
import org.dozer.Mapper;
import org.joda.time.DateTime;
import org.slf4j.Logger;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;
import org.terasoluna.gfw.common.date.DateFactory;
import org.terasoluna.gfw.common.exception.ResourceNotFoundException;

import am.ik.categolj2.domain.model.Category;
import am.ik.categolj2.domain.model.Entry;
import am.ik.categolj2.domain.model.EntryHistory;
import am.ik.categolj2.domain.repository.category.CategoryRepository;
import am.ik.categolj2.domain.repository.entry.EntryHistoryRepository;
import am.ik.categolj2.domain.repository.entry.EntryRepository;

@Service
public class EntryServiceImpl implements EntryService {
    private static final Logger logger = LogManager.getLogger();
    @Inject
    EntryRepository entryRepository;
    @Inject
    EntryHistoryRepository entryHistoryRepository;
    @Inject
    CategoryRepository categoryRepository;
    @Inject
    Mapper beanMapper;
    @Inject
    DateFactory dateFactory;
    Pageable recentPageable = new PageRequest(0, 10);

    @Override
    public Entry findOne(Integer entryId) {
        Entry entry = entryRepository.findDetails(entryId);
        if (entry == null) {
            throw new ResourceNotFoundException(
                    "The request entry is not found. (entryId=" + entryId + ")");
        }
        return entry;
    }

    @Override
    public Entry findOnePublished(Integer entryId) {
        Entry entry = entryRepository.findDetailsPublished(entryId);
        if (entry == null) {
            throw new ResourceNotFoundException(
                    "The request entry is not found. (entryId=" + entryId + ")");
        }
        return entry;
    }

    @Override
    public Page<Entry> findPage(Pageable pageable) {
        Page<Entry> page = entryRepository
                .findPageOrderByLastModifiedDateDesc(pageable);
        applyCategory(page);
        return page;
    }

    void applyCategory(Iterable<Entry> entries) {
        List<Integer> entryIds = new ArrayList<>();
        for (Entry entry : entries) {
            entryIds.add(entry.getId());
        }
        if (!entryIds.isEmpty()) {
            List<Category> categories = categoryRepository.findByEntryIds(entryIds);

            Multimap<Integer, Category> multimap = TreeMultimap.create();
            for (Category c : categories) {
                multimap.put(c.getEntry().getId(), c);
            }
            for (Entry entry : entries) {
                entry.setCategory(new ArrayList<>(multimap.get(entry.getId())));
            }
        }
    }

    @Override
    public Page<Entry> findPagePublished(Pageable pageable) {
        Page<Entry> page = entryRepository
                .findPagePublishedOrderByLastModifiedDateDesc(pageable);
        applyCategory(page);
        return page;
    }

    @Override
    public List<Entry> findAllPublishedUpdatedRecently() {
        List<Entry> entries = entryRepository
                .findAllPublishedOrderByLastModifiedDateDesc(recentPageable);
        return entries;
    }

    @Override
    public Page<Entry> findPagePublishedByCategoryNameAndCategoryOrder(
            String categoryName, Integer categoryOrder, Pageable pageable) {
        Page<Entry> page = entryRepository.findPageDetailsPublishedByCategoryNameAndOrder(
                categoryName, categoryOrder, pageable);
        applyCategory(page);
        return page;
    }

    @Override
    public Page<Entry> findPagePublishedByCreatedBy(String createdBy, Pageable pageable) {
        Page<Entry> page = entryRepository.findPagePublishedByCreatedBy(createdBy, pageable);
        applyCategory(page);
        return page;
    }

    @Override
    public Page<Entry> searchPageByKeyword(String keyword, Pageable pageable) {
        Page<Entry> page = entryRepository.searchPageByKeyword(keyword, pageable);
        applyCategory(page);
        return page;
    }

    @Override
    public Page<Entry> searchPagePublishedByKeyword(String keyword, Pageable pageable) {
        Page<Entry> page = entryRepository.searchPagePublishedByKeyword(keyword, pageable);
        applyCategory(page);
        return page;
    }

    @Override
    @Transactional
    public Entry create(Entry entry, List<Category> category) {
        Assert.notNull(entry, "entry must not be null");
        Assert.isNull(entry.getCategory(), "entry.category must be null");
        Assert.notNull(category, "category must not be null or empty");

        DateTime now = dateFactory.newDateTime();
        entry.setCreatedDate(now);
        entry.setLastModifiedDate(now);
        // createdBy,lastModifiedBy are set by AuditingEntityListener
        entryRepository.saveAndFlush(entry);
        Integer entryId = entry.getEntryId();
        for (Category c : category) {
            c.getCategoryPK().setEntryId(entryId);
        }
        entry.setCategory(category);
        return entry;
    }

    @Override
    @Transactional
    public Entry update(Integer entryId, Entry updatedEntry,
                        boolean updateLastModifiedDate, boolean saveInHistory) {
        Assert.notNull(updatedEntry, "entry must not be null");

        Entry entry = findOne(entryId); // old entry

        if (saveInHistory) {
            logger.info("save history for entryId={}", entryId);
            EntryHistory history = beanMapper.map(entry, EntryHistory.class);
            history.setEntry(entry);
            entryHistoryRepository.save(history);
        }

        if (updateLastModifiedDate) {
            DateTime now = dateFactory.newDateTime();
            updatedEntry.setLastModifiedDate(now);
        }

        // copy new values to entry
        beanMapper.map(updatedEntry, entry);
        entryRepository.save(entry);
        return entry;
    }

    @Override
    @Transactional
    public void delete(Integer entryId) {
        Entry entry = findOne(entryId);
        entryRepository.delete(entry);
    }
}
