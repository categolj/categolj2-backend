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
