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

import am.ik.categolj2.domain.Categolj2AuthorizeAccesses;
import am.ik.categolj2.domain.model.Category;
import am.ik.categolj2.domain.model.Entry;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.prepost.PreAuthorize;

import java.util.List;

public interface EntryService {

    @PreAuthorize(Categolj2AuthorizeAccesses.AUTHORIZED)
    Entry findOne(Integer entryId);

    Entry findOnePublished(Integer entryId);

    @PreAuthorize(Categolj2AuthorizeAccesses.AUTHORIZED)
    Page<Entry> findPage(Pageable pageable);

    Page<Entry> findPagePublished(Pageable pageable);

    List<Entry> findAllPublishedUpdatedRecently();

    Page<Entry> findPagePublishedByCategoryNameAndCategoryOrder(
            String categoryName, Integer categoryOrder, Pageable pageable);

    Page<Entry> findPagePublishedByCreatedBy(String createdBy, Pageable pageable);

    Page<Entry> findPagePublishedByTagName(String tagName, Pageable pageable);

    Page<Entry> findPageByTagName(String tagName, Pageable pageable);

    @PreAuthorize(Categolj2AuthorizeAccesses.AUTHORIZED)
    Page<Entry> searchPageByKeyword(String keyword, Pageable pageable);

    Page<Entry> searchPagePublishedByKeyword(String keyword, Pageable pageable);

    @PreAuthorize(Categolj2AuthorizeAccesses.AUTHORIZED)
    Entry create(Entry entry, List<Category> category);

    @PreAuthorize(Categolj2AuthorizeAccesses.AUTHORIZED)
    Entry update(Integer entryId, Entry updatedEntry,
                 boolean updateLastModifiedDate, boolean saveInHistory);

    @PreAuthorize(Categolj2AuthorizeAccesses.AUTHORIZED)
    void delete(Integer entryId);

}
