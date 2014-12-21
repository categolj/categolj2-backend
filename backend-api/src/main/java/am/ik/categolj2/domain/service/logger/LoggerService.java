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
package am.ik.categolj2.domain.service.logger;

import am.ik.categolj2.domain.Categolj2AuthorizeAccesses;
import org.springframework.security.access.prepost.PreAuthorize;

import java.util.List;

public interface LoggerService {
    @PreAuthorize(Categolj2AuthorizeAccesses.ADMIN_ONLY)
    List<LoggerDto> findAll();

    @PreAuthorize(Categolj2AuthorizeAccesses.ADMIN_ONLY)
    void changeLevel(LoggerDto loggerDto);
}
