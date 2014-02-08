package am.ik.categolj2.domain.service.link;

import am.ik.categolj2.domain.model.Link;

import java.util.List;

public interface LinkService {
    Link findOne(String url);

    List<Link> findAll();

    Link create(Link link);

    Link update(String url, Link link);

    void delete(String url);
}
