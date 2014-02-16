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

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Type;
import org.joda.time.DateTime;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.io.Serializable;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(indexes = {
        @Index(columnList = "METHOD"),
        @Index(columnList = "URI"),
        @Index(columnList = "X_TRACK"),
        @Index(columnList = "ACCESS_DATE")
})
public class AccessLog implements Serializable {
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(generator = "uuid")
    @GenericGenerator(name = "uuid", strategy = "uuid2")
    @Size(min = 36, max = 36)
    @Column(name = "ACCESS_LOG_ID")
    private String accessLogId;

    @Column(name = "METHOD")
    @NotNull
    @Size(min = 1, max = 10)
    private String method;

    @Column(name = "URI")
    @NotNull
    @Size(min = 1, max = 128)
    private String uri;

    @Column(name = "QUERY")
    @Size(min = 1, max = 128)
    private String query;

    @NotNull
    @Size(min = 1, max = 128)
    @Column(name = "REMOTE_ADDRESS")
    private String remoteAddress;

    @NotNull
    @Size(min = 1, max = 128)
    @Column(name = "USER_AGENT", length = 128)
    private String userAgent;

    @Size(min = 32, max = 32)
    @Column(name = "X_TRACK")
    private String xTrack;

    @Column(name = "ACCESS_DATE")
    @Type(type = "org.jadira.usertype.dateandtime.joda.PersistentDateTime")
    private DateTime accessDate;

}
