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

import am.ik.categolj2.core.logger.LogManager;
import ch.qos.logback.classic.Level;
import ch.qos.logback.classic.LoggerContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class LogbackLoggerService implements LoggerService {
    private static Logger logger = LogManager.getLogger();

    @Override
    public synchronized List<LoggerDto> findAll() {
        LoggerContext context = (LoggerContext) LoggerFactory.getILoggerFactory();
        return context.getLoggerList().stream()
                .map(logger -> new LoggerDto(logger.getName(), logger.getEffectiveLevel().toString()))
                .collect(Collectors.toList());
    }

    @Override
    public synchronized void changeLevel(LoggerDto loggerDto) {
        logger.info("change logger level -> {}", loggerDto);
        LoggerContext context = (LoggerContext) LoggerFactory.getILoggerFactory();
        context.getLogger(loggerDto.getName()).setLevel(Level.valueOf(loggerDto.getLevel()));
    }
}
