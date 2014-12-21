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
package am.ik.categolj2.api.user;

import am.ik.categolj2.domain.model.Role;
import am.ik.categolj2.domain.model.User;
import com.google.common.collect.Sets;
import org.dozer.DozerBeanMapper;
import org.dozer.Mapper;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.springframework.core.io.ClassPathResource;

import java.util.*;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

public class RoleToRoleIdConverterTest {
    Mapper mapper = new DozerBeanMapper(
            Arrays.asList("dozer/global-mapping.xml",
                    "dozer/api-mapping.xml"));

    @After
    public void tearDown() throws Exception {
    }

    @Test
    public void testConvertTo() throws Exception {
        User user = new User();
        Set<Role> roles = Sets.newHashSet(new Role(1), new Role(2));
        user.setRoles(roles);

        UserResource userResource = mapper.map(user, UserResource.class);
        assertThat(userResource.getRoles(), is(Arrays.asList(1, 2)));
    }

    @Test
    public void testConvertFrom() throws Exception {
        UserResource userResource = new UserResource();
        userResource.setRoles(Arrays.asList(1, 2));

        User user = mapper.map(userResource, User.class);
        List<Integer> roles = new ArrayList<>();
        for (Role role : user.getRoles()) {
            roles.add(role.getId());
        }
        Collections.sort(roles);
        assertThat(roles, is(userResource.getRoles()));
    }


    @Test
    public void testConvertFrom_forUpdate() throws Exception {
        UserResource userResource = new UserResource();
        userResource.setRoles(new ArrayList(Arrays.asList(1, 2)));


        User user = new User();
        Set<Role> roles = new LinkedHashSet<>();
        {
            Role role = new Role(1);
            role.setRoleName("ROLE_ADMIN");
            roles.add(role);
        }
        {
            Role role = new Role(2);
            role.setRoleName("ROLE_USER");
            roles.add(role);
        }
        user.setRoles(roles);
        System.out.println(user.getRoles().hashCode());
        mapper.map(userResource, user);
        System.out.println(user.getRoles().hashCode());
        System.out.println(user.getRoles());
    }
}
