package am.ik.categolj2.api.link;

import am.ik.categolj2.api.Categolj2Headers;
import am.ik.categolj2.domain.model.Link;
import am.ik.categolj2.domain.service.link.LinkService;
import org.dozer.Mapper;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.HandlerMapping;

import javax.inject.Inject;
import javax.servlet.http.HttpServletRequest;
import java.util.Enumeration;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("links")
public class LinkRestController {
    @Inject
    LinkService linkService;
    @Inject
    Mapper beanMapper;

    @RequestMapping(method = RequestMethod.GET)
    public List<LinkResource> getLinks() {
        List<Link> links = linkService.findAll();
        return links.stream()
                .map(link -> beanMapper.map(link, LinkResource.class))
                .collect(Collectors.toList());
    }

    @RequestMapping(value = "**", method = RequestMethod.GET)
    public LinkResource getLink(HttpServletRequest request) {
        String url = getUrl(request);
        Link link = linkService.findOne(url);
        return beanMapper.map(link, LinkResource.class);
    }

    // admin api

    @RequestMapping(method = RequestMethod.POST, headers = Categolj2Headers.X_ADMIN)
    public ResponseEntity<LinkResource> postLinks(@RequestBody @Validated LinkResource linkResource) {
        Link link = beanMapper.map(linkResource, Link.class);
        Link created = linkService.create(link);
        LinkResource resource = beanMapper.map(created, LinkResource.class);
        return new ResponseEntity<>(resource, HttpStatus.CREATED);
    }

    @RequestMapping(value = "**", method = RequestMethod.PUT, headers = Categolj2Headers.X_ADMIN)
    public LinkResource putLinks(HttpServletRequest request, @RequestBody @Validated LinkResource linkResource) {
        String url = getUrl(request);
        Link link = beanMapper.map(linkResource, Link.class);
        Link updated = linkService.update(url, link);
        return beanMapper.map(updated, LinkResource.class);
    }

    @RequestMapping(value = "**", method = RequestMethod.DELETE, headers = Categolj2Headers.X_ADMIN)
    public ResponseEntity<Void> deleteLinks(HttpServletRequest request) {
        String url = getUrl(request);
        linkService.delete(url);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    String getUrl(HttpServletRequest request) {
        return ((String) request.getAttribute(HandlerMapping.PATH_WITHIN_HANDLER_MAPPING_ATTRIBUTE))
                .replace("/links/", "");
    }
}
