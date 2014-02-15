package am.ik.categolj2.domain.service.link;

import am.ik.categolj2.domain.Categolj2AuthorizeAccesses;
import am.ik.categolj2.domain.model.Link;
import org.springframework.security.access.prepost.PreAuthorize;

import java.util.List;

public interface LinkService {
    Link findOne(String url);

    List<Link> findAll();

    @PreAuthorize(Categolj2AuthorizeAccesses.AUTHORIZED)
    Link create(Link link);

    @PreAuthorize(Categolj2AuthorizeAccesses.AUTHORIZED)
    Link update(String url, Link link);

    @PreAuthorize(Categolj2AuthorizeAccesses.AUTHORIZED)
    void delete(String url);
}
