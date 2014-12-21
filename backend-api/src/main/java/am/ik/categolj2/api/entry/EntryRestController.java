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
package am.ik.categolj2.api.entry;

import am.ik.categolj2.api.ApiVersion;
import am.ik.categolj2.api.Categolj2Headers;
import am.ik.categolj2.domain.model.*;
import am.ik.categolj2.domain.service.accesslog.AccessLogHelper;
import am.ik.categolj2.domain.service.entry.EntryService;
import org.dozer.Mapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.inject.Inject;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("api/" + ApiVersion.CURRENT_VERSION)
public class EntryRestController {
    @Inject
    EntryService entryService;
    @Inject
    AccessLogHelper accessLogHelper;
    @Inject
    Mapper beanMapper;

    // Public API

    @RequestMapping(value = "entries", method = RequestMethod.GET)
    public Page<EntryResource> getEntries(@PageableDefault Pageable pageable, AccessLog accessLog
            , @RequestHeader(value = Categolj2Headers.X_FORMATTED, required = false, defaultValue = "false") boolean isFormatted) {
        accessLogHelper.writeIfAccessLogIsEnabled(accessLog);
        Page<Entry> page = entryService.findPagePublished(pageable);
        return toResourcePage(page, pageable, isFormatted);
    }

    @RequestMapping(value = "entries", method = RequestMethod.GET, params = "keyword")
    public Page<EntryResource> searchEntries(@RequestParam("keyword") String keyword, AccessLog accessLog
            , @PageableDefault Pageable pageable
            , @RequestHeader(value = Categolj2Headers.X_FORMATTED, required = false, defaultValue = "false") boolean isFormatted) {
        accessLogHelper.writeIfAccessLogIsEnabled(accessLog);
        Page<Entry> page = entryService.searchPagePublishedByKeyword(keyword, pageable);
        return toResourcePage(page, pageable, isFormatted);
    }


    @RequestMapping(value = "entries/{entryId}", method = RequestMethod.GET)
    public EntryResource getEntry(@PathVariable("entryId") Integer entryId, AccessLog accessLog
            , @RequestHeader(value = Categolj2Headers.X_FORMATTED, required = false, defaultValue = "false") boolean isFormatted) {
        accessLogHelper.writeIfAccessLogIsEnabled(accessLog);
        Entry entry = entryService.findOnePublished(entryId);
        return toResource(entry, isFormatted);
    }

    @RequestMapping(value = "categories/{category}/entries", method = RequestMethod.GET)
    public Page<EntryResource> getEntriesByCategory(@PathVariable("category") String category, AccessLog accessLog
            , @PageableDefault Pageable pageable
            , @RequestHeader(value = Categolj2Headers.X_FORMATTED, required = false, defaultValue = "false") boolean isFormatted) {
        accessLogHelper.writeIfAccessLogIsEnabled(accessLog);
        Categories categories = Categories.fromCategory(category);
        Integer categoryOrder = categories.getCategories().size();
        String categoryName = categories.getCategories().get(categoryOrder - 1).getCategoryName();
        Page<Entry> page = entryService.findPagePublishedByCategoryNameAndCategoryOrder(categoryName, categoryOrder, pageable);
        return toResourcePage(page, pageable, isFormatted);
    }

    @RequestMapping(value = "users/{createdBy}/entries", method = RequestMethod.GET)
    public Page<EntryResource> getEntriesByCreatedBy(@PathVariable("createdBy") String createdBy, AccessLog accessLog
            , @PageableDefault Pageable pageable
            , @RequestHeader(value = Categolj2Headers.X_FORMATTED, required = false, defaultValue = "false") boolean isFormatted) {
        accessLogHelper.writeIfAccessLogIsEnabled(accessLog);
        Page<Entry> page = entryService.findPagePublishedByCreatedBy(createdBy, pageable);
        return toResourcePage(page, pageable, isFormatted);
    }

    // Admin API

    @RequestMapping(value = "entries", method = RequestMethod.GET, headers = Categolj2Headers.X_ADMIN)
    public Page<EntryResource> getEntriesInAdmin(@PageableDefault Pageable pageable
            , @RequestHeader(value = Categolj2Headers.X_FORMATTED, required = false, defaultValue = "false") boolean isFormatted) {
        Page<Entry> page = entryService.findPage(pageable);
        return toResourcePage(page, pageable, isFormatted);
    }

    @RequestMapping(value = "entries", method = RequestMethod.GET, params = "keyword", headers = Categolj2Headers.X_ADMIN)
    public Page<EntryResource> searchEntriesInAdmin(@RequestParam("keyword") String keyword, @PageableDefault Pageable pageable
            , @RequestHeader(value = Categolj2Headers.X_FORMATTED, required = false, defaultValue = "false") boolean isFormatted) {
        Page<Entry> page = entryService.searchPageByKeyword(keyword, pageable);
        return toResourcePage(page, pageable, isFormatted);
    }

    @RequestMapping(value = "entries/{entryId}", method = RequestMethod.GET, headers = Categolj2Headers.X_ADMIN)
    public EntryResource getEntryInAdmin(@PathVariable("entryId") Integer entryId
            , @RequestHeader(value = Categolj2Headers.X_FORMATTED, required = false, defaultValue = "false") boolean isFormatted) {
        Entry entry = entryService.findOne(entryId);
        return toResource(entry, isFormatted);
    }

    @RequestMapping(value = "entries", method = RequestMethod.POST, headers = Categolj2Headers.X_ADMIN)
    public ResponseEntity<EntryResource> createEntryInAdmin(@RequestBody @Validated EntryResource entryResource
            , @RequestHeader(value = Categolj2Headers.X_FORMATTED, required = false, defaultValue = "false") boolean isFormatted) {
        Entry entry = beanMapper.map(entryResource, Entry.class);
        List<Category> categories = entry.getCategory();
        entry.setCategory(null);
        Entry created = entryService.create(entry, categories);
        return new ResponseEntity<>(toResource(created, isFormatted), HttpStatus.CREATED);
    }

    @RequestMapping(value = "entries/{entryId}", method = RequestMethod.PUT, headers = Categolj2Headers.X_ADMIN)
    public ResponseEntity<EntryResource> updateEntryInAdmin(@PathVariable("entryId") Integer entryId
            , @RequestBody @Validated EntryResource entryResource
            , @RequestHeader(value = Categolj2Headers.X_FORMATTED, required = false, defaultValue = "false") boolean isFormatted) {
        Entry entry = beanMapper.map(entryResource, Entry.class);
        new Categories(entry.getCategory()).applyEntryId(entryId);
        Entry updated = entryService.update(entryId, entry,
                entryResource.isUpdateLastModifiedDate(), entryResource.isSaveInHistory());
        return new ResponseEntity<>(toResource(updated, isFormatted), HttpStatus.OK);
    }

    @RequestMapping(value = "entries/{entryId}", method = RequestMethod.DELETE, headers = Categolj2Headers.X_ADMIN)
    public ResponseEntity<Void> deleteEntryInAdmin(@PathVariable("entryId") Integer entryId) {
        entryService.delete(entryId);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    EntryResource toResource(Entry entry, boolean isFormatted) {
        EntryResource resource = beanMapper.map(entry, EntryResource.class)
                .setCategoryName(entry.getCategory());
        if (isFormatted) {
            EntryFormat format = EntryFormat.valueOf(resource.getFormat().toUpperCase());
            resource.setContents(format.format(resource.getContents()));
        }
        return resource;
    }

    Page<EntryResource> toResourcePage(Page<Entry> page, Pageable pageable, boolean isFormatted) {
        List<EntryResource> resources = page.getContent().stream()
                .map(entry -> toResource(entry, isFormatted))
                .collect(Collectors.toList());
        return new PageImpl<>(resources, pageable, page.getTotalElements());
    }
}
