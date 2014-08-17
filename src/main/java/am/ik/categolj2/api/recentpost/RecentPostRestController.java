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
package am.ik.categolj2.api.recentpost;

import am.ik.categolj2.App;
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
@RequestMapping("api/" + App.API_VERSION + "/recentposts")
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
