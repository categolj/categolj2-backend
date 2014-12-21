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

import am.ik.categolj2.domain.validation.UserEmail;
import am.ik.categolj2.domain.validation.Username;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.NotEmpty;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserResource {
    public static interface Create {
    }

    public static interface Update {
    }

    private String id;
    @NotNull
    @Username
    private String username;
    @NotNull(groups = Create.class)
    @Size(min = 1, max = 256)
    private String password;
    @NotNull
    @UserEmail
    private String email;

    private boolean enabled;

    private boolean locked;
    @NotNull
    @Size(min = 1, max = 128)
    private String firstName;
    @NotNull
    @Size(min = 1, max = 128)
    private String lastName;

    @NotEmpty
    private List<Integer> roles;
}
