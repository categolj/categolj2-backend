package am.ik.categolj2.api.entry;

import am.ik.categolj2.api.Categolj2Headers;
import am.ik.categolj2.domain.model.Entry;
import am.ik.categolj2.domain.model.EntryHistory;
import am.ik.categolj2.domain.service.entry.EntryService;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.inject.Inject;
import java.util.List;

@RestController
@RequestMapping("entries/{entryId}/histories")
public class EntryHistoryRestController {
    @Inject
    EntryService entryService;

    @RequestMapping(method = RequestMethod.GET, headers = Categolj2Headers.X_ADMIN)
    public List<EntryHistory> getEntryHistoriesInAdmin(@PathVariable("entryId") Integer entryId) {
        Entry entry = entryService.findOne(entryId);
        return entry.getHistories();
    }
}
