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

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import am.ik.categolj2.domain.model.User;

public interface UserRepository extends JpaRepository<User, String> {
    @Query(value = "SELECT DISTINCT x FROM User x JOIN FETCH x.roles WHERE x.username = :username")
    User findDetails(@Param("username") String username);

    @Query(value = "SELECT DISTINCT x FROM User x JOIN FETCH x.roles WHERE x.email = :email")
    User findOneByEmail(@Param("email") String email);

    @Query("SELECT x.username FROM User x WHERE x.email = :email")
    String findUsernameByEmail(@Param("email") String email);

    long countByEmail(String email);

    @Query("SELECT COUNT(x) FROM User x WHERE x.email = :email AND x.username != :username")
    long countByEmailOtherThanMe(@Param("email") String email, @Param("username") String username);

    @Query("SELECT COUNT(x) FROM User x JOIN x.roles r " +
            "WHERE x.username != :username " +
            "AND r.roleName = 'ADMIN' " +
            "AND x.enabled = true " +
            "AND x.locked = false")
    long countActiveAdminOtherThanMe(@Param("username") String username);
}
