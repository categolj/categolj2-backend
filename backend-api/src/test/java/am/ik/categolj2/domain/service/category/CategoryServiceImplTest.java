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
package am.ik.categolj2.domain.service.category;

import am.ik.categolj2.App;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.IntegrationTest;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import javax.inject.Inject;

@RunWith(SpringJUnit4ClassRunner.class)
// TODO fix slow test
@SpringApplicationConfiguration(classes = App.class)
@IntegrationTest({"server.port:0",
        "server-http.port:0",
        "spring.datasource.url:jdbc:h2:mem:bookmark;DB_CLOSE_ON_EXIT=FALSE",
        "flyway.enabled:false",
        "spring.jpa.hibernate.generate-ddl:true",
        "spring.jpa.hibernate.ddl-auto:create-drop",
        "endpoints.autoconfig.enabled:false",
        "endpoints.beans.enabled:false",
        "endpoints.configprops.enabled:false",
        "endpoints.dump.enabled:false",
        "endpoints.env.enabled:false",
        "endpoints.health.enabled:false",
        "endpoints.metrics.enabled:false",
        "endpoints.shutdown.enabled:false",
        "endpoints.trace.enabled:false",
        "endpoints.jmx.enabled:false"})
public class CategoryServiceImplTest {
    @Inject
    CategoryService categoryService;

    @Test
    public void testFindAll() throws Exception {
        System.out.println(categoryService.findAll());
    }

    @Test
    public void testSearch() throws Exception {
        System.out.println(categoryService.search("java"));
    }
}
