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
package am.ik.categolj2.domain.service.user;

import am.ik.categolj2.domain.Categolj2AuthorizeAccesses;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import am.ik.categolj2.domain.model.User;
import org.springframework.security.access.prepost.PreAuthorize;

public interface UserService {
    User findOne(String username);

    User findOneByUsernameOrEmail(String usernameOrEmail);

    @PreAuthorize(Categolj2AuthorizeAccesses.ADMIN_ONLY)
    Page<User> findPage(Pageable pageable);

    @PreAuthorize(Categolj2AuthorizeAccesses.ADMIN_ONLY)
    User create(User user, String rawPassword);

    @PreAuthorize(Categolj2AuthorizeAccesses.AUTHORIZED)
    User update(String username, User updatedUser, String updatedRawPassword);

    @PreAuthorize(Categolj2AuthorizeAccesses.ADMIN_ONLY)
    User updateWithoutPassword(String username, User updatedUser);

    @PreAuthorize(Categolj2AuthorizeAccesses.ADMIN_ONLY)
    void delete(String username);
}
