package am.ik.categolj2.api.entry;


import am.ik.categolj2.domain.model.AccessLog;
import am.ik.categolj2.domain.model.Entry;
import am.ik.categolj2.domain.service.accesslog.AccessLogHelper;
import am.ik.categolj2.domain.service.entry.EntryService;
import com.codahale.metrics.annotation.Timed;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.util.UriComponentsBuilder;

import javax.inject.Inject;

@RequestMapping
@Controller
public class EntryPdfController {
    @Inject
    EntryService entryService;
    @Inject
    AccessLogHelper accessLogHelper;

    @RequestMapping(value = "entries/{entryId}.pdf", method = RequestMethod.GET)
    @Timed
    public String downloadPdf(@PathVariable("entryId") Integer entryId, AccessLog accessLog, Model model, UriComponentsBuilder uriBuilder) {
        accessLogHelper.writeIfAccessLogIsEnabled(accessLog);
        Entry entry = entryService.findOnePublished(entryId);
        model.addAttribute(entry);
        model.addAttribute("uriComponentsBuilder", uriBuilder);
        return "entryPdfView";
    }

}
