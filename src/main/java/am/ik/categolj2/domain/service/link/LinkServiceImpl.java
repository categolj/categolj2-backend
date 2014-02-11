package am.ik.categolj2.domain.service.link;


import am.ik.categolj2.domain.model.Link;
import am.ik.categolj2.domain.repository.link.LinkRepository;
import org.dozer.Mapper;
import org.joda.time.DateTime;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.terasoluna.gfw.common.date.DateFactory;
import org.terasoluna.gfw.common.exception.ResourceNotFoundException;

import javax.inject.Inject;
import java.util.List;

@Service
public class LinkServiceImpl implements LinkService {
    @Inject
    LinkRepository linkRepository;
    @Inject
    DateFactory dateFactory;
    @Inject
    Mapper beanMapper;

    @Override
    public Link findOne(String url) {
        Link link = linkRepository.findOne(url);
        if (link == null) {
            throw new ResourceNotFoundException("link is not found. [url="
                    + url + "]");
        }
        return link;
    }

    @Override
    public List<Link> findAll() {
        return linkRepository.findAllOrderByLastModifiedDateDesc();
    }

    @Transactional
    @Override
    public Link create(Link link) {
        DateTime now = dateFactory.newDateTime();
        link.setCreatedDate(now);
        link.setLastModifiedDate(now);
        return linkRepository.save(link);
    }

    @Transactional
    @Override
    public Link update(String url, Link link) {
        DateTime now = dateFactory.newDateTime();
        Link old = linkRepository.findOne(url);
        beanMapper.map(link, old);
        link.setLastModifiedDate(now);
        return linkRepository.save(old);
    }

    @Transactional
    @Override
    public void delete(String url) {
        linkRepository.delete(url);
    }
}
