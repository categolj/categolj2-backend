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
package am.ik.categolj2.infra.cache;

import am.ik.categolj2.core.logger.LogManager;
import org.slf4j.Logger;
import org.springframework.cache.Cache;

public class LoggingCacheAdaptor implements Cache {
    private static final Logger logger = LogManager.getLogger();
    private final Cache targetCache;
    private final String name;

    public LoggingCacheAdaptor(Cache targetCache) {
        this.targetCache = targetCache;
        this.name = targetCache.getName();
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public Object getNativeCache() {
        return targetCache.getNativeCache();
    }

    @Override
    public ValueWrapper get(Object key) {
        logger.debug("[{}]\tget key={}", name, key);
        return targetCache.get(key);
    }

    @Override
    public <T> T get(Object key, Class<T> type) {
        logger.debug("[{}]\tget key={},type={}", name, key, type);
        return targetCache.get(key, type);
    }

    @Override
    public void put(Object key, Object value) {
        logger.debug("[{}]\tput key={}", name, key);
        targetCache.put(key, value);
    }

    @Override
    public void evict(Object key) {
        logger.debug("[{}]\tevict key={}", name, key);
        targetCache.evict(key);
    }

    @Override
    public void clear() {
        logger.debug("[{}]\tclear", name);
        targetCache.clear();
    }

    @Override
    public ValueWrapper putIfAbsent(Object key, Object value) {
        logger.debug("[{}]\tputIfAbsent key={}", name, key);
        return targetCache.putIfAbsent(key, value);
    }
}
