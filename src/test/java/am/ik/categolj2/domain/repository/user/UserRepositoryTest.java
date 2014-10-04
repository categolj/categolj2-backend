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
package am.ik.categolj2.domain.repository.user;

import static org.junit.Assert.*;
import static org.hamcrest.CoreMatchers.*;

import javax.inject.Inject;

import am.ik.categolj2.App;
import org.joda.time.DateTime;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.IntegrationTest;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;

import am.ik.categolj2.domain.model.User;

@RunWith(SpringJUnit4ClassRunner.class)
// TODO fix slow test
@SpringApplicationConfiguration(classes = App.class)
@IntegrationTest({"server.port:0",
        "spring.datasource.url:jdbc:h2:mem:bookmark;DB_CLOSE_ON_EXIT=FALSE"})
public class UserRepositoryTest {
    DateTime now = new DateTime();
    @Inject
    UserRepository userRepository;

    @Before
    // @Rollback(false)
    @Transactional
    public void setUp() {
        User user = new User("user1", "user1", "user1@example.com", true,
                false, "User1", "Name1", null);
        user.setCreatedDate(now);
        user.setLastModifiedDate(now);
        try {
            userRepository.save(user);
        } catch (DataIntegrityViolationException e) {
            // already inserted
        }
    }

    @Test
    // @Rollback(false)
    @Transactional
    public void testSave() {
        User user = new User("making", "hoge", "makingx@gmail.com", true,
                false, "Toshiaki", "Maki", null);
        user.setCreatedDate(now);
        user.setLastModifiedDate(now);
        userRepository.save(user);
    }

    @Test
    @Transactional
    public void testFindOne() {
        User user = userRepository.findOne("admin");
        System.out.println(user.getRoles());
        assertThat(user, is(notNullValue()));
    }

    @Test
    public void testFindAll() {
        System.out.println(userRepository.findAll());
    }


    @Test
    public void testCountAdmin() {
        System.out.println(userRepository.countActiveAdminOtherThanMe("admin"));
    }
}
