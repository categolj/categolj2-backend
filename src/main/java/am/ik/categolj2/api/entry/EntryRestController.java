package am.ik.categolj2.api.entry;

import javax.inject.Inject;

import am.ik.categolj2.api.Categolj2Headers;
import am.ik.categolj2.domain.model.Categories;
import am.ik.categolj2.domain.model.Category;
import org.dozer.Mapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import am.ik.categolj2.domain.model.Entry;
import am.ik.categolj2.domain.service.entry.EntryService;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping
public class EntryRestController {
    @Inject
    EntryService entryService;
    @Inject
    Mapper beanMapper;

    // Public API

    @RequestMapping(value = "entries", method = RequestMethod.GET)
    public Page<EntryResource> getEntries(@PageableDefault Pageable pageable) {
        Page<Entry> page = entryService.findPagePublished(pageable);
        return toResourcePage(page, pageable);
    }

    @RequestMapping(value = "entries", method = RequestMethod.GET, params = "keyword")
    public Page<EntryResource> searchEntries(@RequestParam("keyword") String keyword, @PageableDefault Pageable pageable) {
        // TODO search
        Page<Entry> page = entryService.findPagePublished(pageable);
        return toResourcePage(page, pageable);
    }

    @RequestMapping(value = "entries/{entryId}", method = RequestMethod.GET)
    public EntryResource getEntry(@PathVariable("entryId") Integer entryId) {
        Entry entry = entryService.findOnePublished(entryId);
        return toResource(entry);
    }

    @RequestMapping(value = "categories/{category}/entries", method = RequestMethod.GET)
    public Page<EntryResource> getEntriesByCategory(@PathVariable("category") String category, @PageableDefault Pageable pageable) {
        Categories categories = Categories.fromCategory(category);
        Integer categoryOrder = categories.getCategories().size();
        String categoryName = categories.getCategories().get(categoryOrder - 1).getCategoryName();
        Page<Entry> page = entryService.findPagePublishedByCategoryNameAndCategoryOrder(categoryName, categoryOrder, pageable);
        return toResourcePage(page, pageable);
    }

    @RequestMapping(value = "users/{createdBy}/entries", method = RequestMethod.GET)
    public Page<EntryResource> getEntriesByCreatedBy(@PathVariable("createdBy") String createdBy, @PageableDefault Pageable pageable) {
        Page<Entry> page = entryService.findPagePublishedByCreatedBy(createdBy, pageable);
        return toResourcePage(page, pageable);
    }

    // Admin API

    @RequestMapping(value = "entries", method = RequestMethod.GET, headers = Categolj2Headers.X_ADMIN)
    public Page<EntryResource> getEntriesInAdmin(@PageableDefault Pageable pageable) {
        Page<Entry> page = entryService.findPage(pageable);
        return toResourcePage(page, pageable);
    }

    @RequestMapping(value = "entries", method = RequestMethod.GET, params = "keyword", headers = Categolj2Headers.X_ADMIN)
    public Page<EntryResource> searchEntriesInAdmin(@RequestParam("keyword") String keyword, @PageableDefault Pageable pageable) {
        Page<Entry> page = entryService.searchPageByKeyword(keyword, pageable);
        return toResourcePage(page, pageable);
    }

    @RequestMapping(value = "entries/{entryId}", method = RequestMethod.GET, headers = Categolj2Headers.X_ADMIN)
    public EntryResource getEntryInAdmin(@PathVariable("entryId") Integer entryId) {
        Entry entry = entryService.findOne(entryId);
        return toResource(entry);
    }

    @RequestMapping(value = "entries", method = RequestMethod.POST, headers = Categolj2Headers.X_ADMIN)
    public ResponseEntity<EntryResource> createEntryInAdmin(@RequestBody @Validated EntryResource entryResource) {
        Entry entry = beanMapper.map(entryResource, Entry.class);
        List<Category> categories = entry.getCategory();
        entry.setCategory(null);
        Entry created = entryService.create(entry, categories);
        return new ResponseEntity<>(toResource(created), HttpStatus.CREATED);
    }

    @RequestMapping(value = "entries/{entryId}", method = RequestMethod.PUT, headers = Categolj2Headers.X_ADMIN)
    public ResponseEntity<EntryResource> updateEntryInAdmin(@PathVariable("entryId") Integer entryId, @RequestBody @Validated EntryResource entryResource) {
        Entry entry = beanMapper.map(entryResource, Entry.class);
        new Categories(entry.getCategory()).applyEntryId(entryId);
        Entry updated = entryService.update(entryId, entry,
                entryResource.isUpdateLastModifiedDate(), entryResource.isSaveInHistory());
        return new ResponseEntity<>(toResource(updated), HttpStatus.OK);
    }

    @RequestMapping(value = "entries/{entryId}", method = RequestMethod.DELETE, headers = Categolj2Headers.X_ADMIN)
    public ResponseEntity<Void> deleteEntryInAdmin(@PathVariable("entryId") Integer entryId) {
        entryService.delete(entryId);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    EntryResource toResource(Entry entry) {
        return beanMapper.map(entry, EntryResource.class)
                .setCategoryName(entry.getCategory());
    }

    Page<EntryResource> toResourcePage(Page<Entry> page, Pageable pageable) {
        List<EntryResource> resources = page.getContent().stream()
                .map(this::toResource)
                .collect(Collectors.toList());
        return new PageImpl<>(resources, pageable, page.getTotalElements());
    }
}
