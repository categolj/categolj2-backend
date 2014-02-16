package am.ik.categolj2.app.feed;

import java.util.List;

import javax.inject.Inject;

import am.ik.categolj2.domain.model.Entry;
import am.ik.categolj2.domain.service.entry.EntryService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class FeedController {
    @Inject
    EntryService entryService;

    @RequestMapping({"/feed", "/rss"})
    public String feed(Model model) {
        List<Entry> entries = entryService.findAllPublishedUpdatedRecently();
        model.addAttribute("entries", entries);
        return "rssEntryFeedView";
    }
}
