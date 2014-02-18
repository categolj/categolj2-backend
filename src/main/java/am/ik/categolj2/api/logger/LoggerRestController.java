package am.ik.categolj2.api.logger;

import am.ik.categolj2.api.Categolj2Headers;
import am.ik.categolj2.domain.service.logger.LoggerDto;
import am.ik.categolj2.domain.service.logger.LoggerService;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.inject.Inject;
import java.util.List;

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
@RestController
@RequestMapping("loggers")
public class LoggerRestController {
    @Inject
    LoggerService loggerService;

    @RequestMapping(method = RequestMethod.GET, headers = Categolj2Headers.X_ADMIN)
    public List<LoggerDto> getLoggers() {
        return loggerService.findAll();
    }

    @RequestMapping(method = RequestMethod.PUT, headers = Categolj2Headers.X_ADMIN)
    public void putLogger(@Validated @RequestBody LoggerDto loggerDto) {
        loggerService.changeLevel(loggerDto);
    }
}
