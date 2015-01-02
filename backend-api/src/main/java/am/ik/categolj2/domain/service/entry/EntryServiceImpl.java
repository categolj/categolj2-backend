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

import am.ik.categolj2.core.message.MessageKeys;
import am.ik.categolj2.domain.model.Category;
import am.ik.categolj2.domain.model.Entry;
import am.ik.categolj2.domain.model.EntryHistory;
import am.ik.categolj2.domain.model.Tag;
import am.ik.categolj2.domain.repository.category.CategoryRepository;
import am.ik.categolj2.domain.repository.entry.EntryHistoryRepository;
import am.ik.categolj2.domain.repository.entry.EntryRepository;
import am.ik.categolj2.domain.repository.tag.TagAndEntryId;
import am.ik.categolj2.domain.repository.tag.TagRepository;
import com.google.common.collect.HashMultimap;
import com.google.common.collect.Multimap;
import com.google.common.collect.Sets;
import com.google.common.collect.TreeMultimap;
import lombok.extern.slf4j.Slf4j;
import org.dozer.Mapper;
import org.joda.time.DateTime;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;
import org.terasoluna.gfw.common.date.DateFactory;
import org.terasoluna.gfw.common.exception.ResourceNotFoundException;
import org.terasoluna.gfw.common.message.ResultMessages;

import javax.inject.Inject;
import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

@Service
@Slf4j
public class EntryServiceImpl implements EntryService {
    @Inject
    EntryRepository entryRepository;
    @Inject
    EntryHistoryRepository entryHistoryRepository;
    @Inject
    CategoryRepository categoryRepository;
    @Inject
    TagRepository tagRepository;
    @Inject
    Mapper beanMapper;
    @Inject
    DateFactory dateFactory;
    Pageable recentPageable = new PageRequest(0, 10);

    @Override
    public Entry findOne(Integer entryId) {
        Entry entry = entryRepository.findDetails(entryId);
        if (entry == null) {
            ResultMessages messages = ResultMessages.error()
                    .add(MessageKeys.E_CT_EN_8201, entryId);
            throw new ResourceNotFoundException(messages);
        }
        return entry;
    }

    @Override
    @Cacheable("entry")
    public Entry findOnePublished(Integer entryId) {
        Entry entry = entryRepository.findDetailsPublished(entryId);
        if (entry == null) {
            ResultMessages messages = ResultMessages.error()
                    .add(MessageKeys.E_CT_EN_8201, entryId);
            throw new ResourceNotFoundException(messages);
        }
        return entry;
    }

    @Override
    public Page<Entry> findPage(Pageable pageable) {
        Page<Entry> page = entryRepository
                .findPageOrderByLastModifiedDateDesc(pageable);
        applyRelations(page);
        return page;
    }

    void applyRelations(Iterable<Entry> entries) {
        List<Integer> entryIds = StreamSupport.stream(entries.spliterator(), false)
                .map(Entry::getEntryId)
                .collect(Collectors.toList());
        if (!entryIds.isEmpty()) {
            // apply categories
            List<Category> categories = categoryRepository.findByEntryIds(entryIds);

            Multimap<Integer, Category> categoryMultimap = TreeMultimap.create();
            for (Category c : categories) {
                categoryMultimap.put(c.getEntry().getId(), c);
            }
            for (Entry entry : entries) {
                entry.setCategory(new ArrayList<>(categoryMultimap.get(entry.getId())));
            }
            // apply tags
            List<TagAndEntryId> tags = tagRepository.findByEntryIds(entryIds);
            Multimap<Integer, Tag> tagMultimap = HashMultimap.create();
            for (TagAndEntryId tag : tags) {
                tagMultimap.put(tag.getEntryId(), tag.getTag());
            }
            for (Entry entry : entries) {
                entry.setTags(new LinkedHashSet<>(tagMultimap.get(entry.getEntryId())));
            }
        }
    }

    @Override
    @Cacheable("entry")
    public Page<Entry> findPagePublished(Pageable pageable) {
        Page<Entry> page = entryRepository
                .findPagePublishedOrderByLastModifiedDateDesc(pageable);
        applyRelations(page);
        return page;
    }

    @Override
    @Cacheable("recentPost")
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
        applyRelations(page);
        return page;
    }

    @Override
    public Page<Entry> findPagePublishedByCreatedBy(String createdBy, Pageable pageable) {
        Page<Entry> page = entryRepository.findPagePublishedByCreatedBy(createdBy, pageable);
        applyRelations(page);
        return page;
    }

    @Override
    public Page<Entry> findPagePublishedByTagName(String tagName, Pageable pageable) {
        Page<Entry> page = entryRepository.findPageByTags_TagNameAndPublishedTrueOrderByLastModifiedDateDesc(tagName, pageable);
        applyRelations(page);
        return page;
    }

    @Override
    public Page<Entry> findPageByTagName(String tagName, Pageable pageable) {
        Page<Entry> page = entryRepository.findPageByTags_TagNameOrderByLastModifiedDateDesc(tagName, pageable);
        applyRelations(page);
        return page;
    }

    @Override
    public Page<Entry> searchPageByKeyword(String keyword, Pageable pageable) {
        Page<Entry> page = entryRepository.searchPageByKeyword(keyword, pageable);
        applyRelations(page);
        return page;
    }

    @Override
    public Page<Entry> searchPagePublishedByKeyword(String keyword, Pageable pageable) {
        Page<Entry> page = entryRepository.searchPagePublishedByKeyword(keyword, pageable);
        applyRelations(page);
        return page;
    }

    @Override
    @Transactional
    @CacheEvict(value = {"recentPost", "entry"}, allEntries = true)
    public Entry create(Entry entry, List<Category> category) {
        Assert.notNull(entry, "entry must not be null");
        Assert.isNull(entry.getCategory(), "entry.category must be null");
        Assert.notNull(category, "category must not be null or empty");
        Assert.notNull(entry.getTags(), "category must not be null");
        // create new tags
        tagRepository.save(entry.getTags().stream()
                .filter(tag -> !tagRepository.exists(tag.getTagName()))
                .collect(Collectors.toList()));
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
    @CacheEvict(value = {"recentPost", "entry"}, allEntries = true)
    public Entry update(Integer entryId, Entry updatedEntry,
                        boolean updateLastModifiedDate, boolean saveInHistory) {
        Assert.notNull(updatedEntry, "entry must not be null");

        Entry entry = findOne(entryId); // old entry

        if (saveInHistory) {
            log.info("save history for entryId={}", entryId);
            EntryHistory history = beanMapper.map(entry, EntryHistory.class);
            history.setEntry(entry);
            entryHistoryRepository.save(history);
        }

        if (updateLastModifiedDate) {
            DateTime now = dateFactory.newDateTime();
            updatedEntry.setLastModifiedDate(now);
        }

        Set<Tag> reduced = Sets.difference(entry.getTags(), updatedEntry.getTags());
        houseKeepTag(reduced);
        // copy new values to entry
        beanMapper.map(updatedEntry, entry);
        entryRepository.save(entry);
        return entry;
    }

    @Override
    @Transactional
    @CacheEvict(value = {"recentPost", "entry"}, allEntries = true)
    public void delete(Integer entryId) {
        Entry entry = findOne(entryId);
        houseKeepTag(entry.getTags());
        entryRepository.delete(entry);
    }

    void houseKeepTag(Set<Tag> tags) {
        tagRepository.delete(tags.stream()
                .filter(tag -> tagRepository.countByTagName(tag.getTagName()) == 1)
                .peek(tag -> log.info("Delete unused tag -> {}", tag))
                .collect(Collectors.toList()));
    }
}
