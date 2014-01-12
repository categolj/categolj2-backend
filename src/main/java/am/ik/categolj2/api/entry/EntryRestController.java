package am.ik.categolj2.api.entry;

import javax.inject.Inject;

import am.ik.categolj2.api.Categolj2Headers;
import am.ik.categolj2.app.entry.EntryForm;
import am.ik.categolj2.domain.model.Categories;
import am.ik.categolj2.domain.model.Category;
import org.dozer.Mapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import am.ik.categolj2.domain.model.Entry;
import am.ik.categolj2.domain.service.entry.EntryService;

import java.util.List;

@Controller
@RequestMapping("entries")
public class EntryRestController {
    @Inject
    EntryService entryService;
    @Inject
    Mapper beanMapper;

    // Public API

    @RequestMapping(method = RequestMethod.GET)
    @ResponseBody
    public Page<Entry> getEntries(@PageableDefault Pageable pageable) {
        Page<Entry> page = entryService.findPagePublished(pageable);
        return page;
    }

    @RequestMapping(value = "{entryId}", method = RequestMethod.GET)
    @ResponseBody
    public Entry getEntry(@PathVariable("entryId") Integer entryId) {
        Entry entry = entryService.findOnePublished(entryId);
        return entry;
    }

    // Admin API

    @RequestMapping(method = RequestMethod.GET, headers = Categolj2Headers.X_ADMIN)
    @ResponseBody
    public Page<Entry> getEntriesInAdmin(@PageableDefault Pageable pageable) {
        Page<Entry> page = entryService.findPage(pageable);
        return page;
    }

    @RequestMapping(value = "{entryId}", method = RequestMethod.GET, headers = Categolj2Headers.X_ADMIN)
    @ResponseBody
    public Entry getEntryInAdmin(@PathVariable("entryId") Integer entryId) {
        Entry entry = entryService.findOne(entryId);
        return entry;
    }

    @RequestMapping(method = RequestMethod.POST, headers = Categolj2Headers.X_ADMIN)
    @ResponseBody
    public ResponseEntity<Entry> createEntryInAdmin(@RequestBody @Validated EntryForm form) {
        Entry entry = beanMapper.map(form, Entry.class);
        List<Category> categories = entry.getCategory();
        entry.setCategory(null);
        Entry created = entryService.create(entry, categories);
        return new ResponseEntity<>(created, HttpStatus.CREATED);
    }

    @RequestMapping(value = "{entryId}", method = RequestMethod.PUT, headers = Categolj2Headers.X_ADMIN)
    @ResponseBody
    public ResponseEntity<Entry> updteEntryInAdmin(@PathVariable("entryId") Integer entryId, @RequestBody @Validated EntryForm form) {
        Entry entry = beanMapper.map(form, Entry.class);
        new Categories(entry.getCategory()).applyEntryId(entryId);
        Entry updated = entryService.update(entryId, entry,
                form.isUpdateLastModifiedDate(), form.isSaveInHistory());
        return new ResponseEntity<>(updated, HttpStatus.OK);
    }

    @RequestMapping(value = "{entryId}", method = RequestMethod.DELETE, headers = Categolj2Headers.X_ADMIN)
    @ResponseBody
    public ResponseEntity<Void> deleteEntryInAdmin(@PathVariable("entryId") Integer entryId) {
        entryService.delete(entryId);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}
