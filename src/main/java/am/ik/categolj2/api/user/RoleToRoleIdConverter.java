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
import org.dozer.DozerConverter;

import java.util.List;
import java.util.Set;

public class RoleToRoleIdConverter extends DozerConverter<Role, Integer> {

    public RoleToRoleIdConverter() {
        super(Role.class, Integer.class);
    }


    @Override
    public Integer convertTo(Role source, Integer destination) {
        if (source == null) {
            return null;
        }
        return source.getId();
    }

    @Override
    public Role convertFrom(Integer source, Role destination) {
        if (source == null) {
            return null;
        }
        return new Role(source);
    }
}
