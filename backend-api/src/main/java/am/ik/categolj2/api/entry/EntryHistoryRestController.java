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
package am.ik.categolj2.api.entry;

import am.ik.categolj2.api.ApiVersion;
import am.ik.categolj2.api.Categolj2Headers;
import am.ik.categolj2.domain.model.Entry;
import am.ik.categolj2.domain.model.EntryHistory;
import am.ik.categolj2.domain.service.entry.EntryService;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.inject.Inject;
import java.util.List;

@RestController
@RequestMapping("api/" + ApiVersion.CURRENT_VERSION + "/entries/{entryId}/histories")
public class EntryHistoryRestController {
    @Inject
    EntryService entryService;

    @RequestMapping(method = RequestMethod.GET, headers = Categolj2Headers.X_ADMIN)
    public List<EntryHistory> getEntryHistoriesInAdmin(@PathVariable("entryId") Integer entryId) {
        Entry entry = entryService.findOne(entryId);
        return entry.getHistories();
    }
}
