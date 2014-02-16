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
package am.ik.categolj2.domain.repository.category;

import static org.junit.Assert.*;


import javax.inject.Inject;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;

import am.ik.categolj2.domain.repository.entry.EntryRepository;
import org.terasoluna.gfw.common.query.QueryEscapeUtils;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"classpath:test-context.xml"})
public class CategoryRepositoryTest {
    @Inject
    CategoryRepository categoryRepository;

    @Inject
    EntryRepository entryRepository;

    @Test
    public void test() {
        System.out.println(categoryRepository.findAllConcatenatedCategory());
    }

    @Test
    public void testLike() {
        System.out.println(categoryRepository.findConcatenatedCategoryLikeCategoryName(QueryEscapeUtils.toContainingCondition("Java")));
    }
}
