/*
 * Copyright (C) 2014 Toshiaki Maki
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND,
 * either express or implied. See the License for the specific language
 * governing permissions and limitations under the License.
 */
package am.ik.categolj2.domain.service.link;


import am.ik.categolj2.core.message.MessageKeys;
import am.ik.categolj2.domain.model.Link;
import am.ik.categolj2.domain.repository.link.LinkRepository;
import org.apache.commons.lang.builder.ToStringBuilder;
import org.dozer.Mapper;
import org.joda.time.DateTime;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.terasoluna.gfw.common.date.DateFactory;
import org.terasoluna.gfw.common.exception.ResourceNotFoundException;
import org.terasoluna.gfw.common.message.ResultMessages;

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
            ResultMessages messages = ResultMessages.error()
                    .add(MessageKeys.E_CT_LN_8301, url);
            throw new ResourceNotFoundException(messages);
        }
        return link;
    }

    @Override
    @Cacheable("link")
    public List<Link> findAll() {
        return linkRepository.findAllOrderByLastModifiedDateDesc();
    }

    @Transactional
    @CacheEvict(value = {"link"}, allEntries = true)
    @Override
    public Link create(Link link) {
        DateTime now = dateFactory.newDateTime();
        link.setCreatedDate(now);
        link.setLastModifiedDate(now);
        return linkRepository.save(link);
    }

    @Transactional
    @CacheEvict(value = {"link"}, allEntries = true)
    @Override
    public Link update(String url, Link link) {
        DateTime now = dateFactory.newDateTime();
        Link old = linkRepository.findOne(url);
        beanMapper.map(link, old);
        old.setLastModifiedDate(now);
        return linkRepository.save(old);
    }

    @Transactional
    @CacheEvict(value = {"link"}, allEntries = true)
    @Override
    public void delete(String url) {
        linkRepository.delete(url);
    }
}
