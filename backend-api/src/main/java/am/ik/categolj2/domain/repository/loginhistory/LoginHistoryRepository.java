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
package am.ik.categolj2.domain.repository.loginhistory;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import am.ik.categolj2.domain.model.LoginHistory;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface LoginHistoryRepository extends
        JpaRepository<LoginHistory, String> {
    @Query("SELECT x FROM LoginHistory x ORDER BY x.loginDate DESC")
    Page<LoginHistory> findPageOrderByLoginDateDesc(Pageable pageable);
}
