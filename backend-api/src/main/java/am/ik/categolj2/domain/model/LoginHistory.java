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
package am.ik.categolj2.domain.model;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Type;
import org.joda.time.DateTime;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Data
@EqualsAndHashCode(callSuper = false)
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "LOGIN_HISTORY", indexes = {
        @javax.persistence.Index(columnList = "LOGIN_DATE")
})
public class LoginHistory {
    @Id
    @GeneratedValue(generator = "uuid")
    @GenericGenerator(name = "uuid", strategy = "uuid2")
    @Size(min = 36, max = 36)
    @Column(name = "LOGIN_HISTORY_ID")
    private String loginHistoryId;

    @NotNull
    @Size(min = 1, max = 128)
    @Column(name = "USERNAME", length = 128)
    private String username;

    @NotNull
    @Size(min = 1, max = 128)
    @Column(name = "LOGIN_HOST")
    private String loginHost;

    @NotNull
    @Size(min = 1, max = 256)
    @Column(name = "LOGIN_AGENT", length = 128)
    private String loginAgent;

    @NotNull
    @Column(name = "LOGIN_DATE")
    @Type(type = "org.jadira.usertype.dateandtime.joda.PersistentDateTime")
    private DateTime loginDate;
}
