package am.ik.categolj2.api.recentpost;

import am.ik.categolj2.domain.model.Entry;
import am.ik.categolj2.domain.service.entry.EntryService;
import org.dozer.Mapper;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.inject.Inject;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("recentposts")
public class RecentPostRestController {
    @Inject
    EntryService entryService;

    @Inject
    Mapper beanMapper;

    @RequestMapping(method = RequestMethod.GET)
    public List<RecentPostResource> getRecentPosts() {
        List<Entry> entries = entryService.findAllPublishedUpdatedRecently();
        return entries.stream()
                .map(entry -> beanMapper.map(entry, RecentPostResource.class))
                .collect(Collectors.toList());
    }
}
