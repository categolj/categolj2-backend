package am.ik.categolj2.api.entry;

import javax.inject.Inject;

import am.ik.categolj2.app.entry.EntryForm;
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

    @RequestMapping(method = RequestMethod.GET)
    @ResponseBody
    public Page<Entry> getEntry(@PageableDefault Pageable pageable) {
        Page<Entry> page = entryService.findPage(pageable);
        return page;
    }

    @RequestMapping(value = "{entryId}", method = RequestMethod.GET)
    @ResponseBody
    public Entry getEntry(@PathVariable("entryId") Integer entryId) {
        Entry entry = entryService.findOnePublished(entryId);
        return entry;
    }

    @RequestMapping(method = RequestMethod.POST)
    @ResponseBody
    public ResponseEntity<Entry> create(@RequestBody @Validated EntryForm form) {
        Entry entry = beanMapper.map(form, Entry.class);
        List<Category> categories = entry.getCategory();
        entry.setCategory(null);
        Entry created = entryService.create(entry, categories);
        return new ResponseEntity<Entry>(created, HttpStatus.CREATED);
    }
}
