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
package am.ik.categolj2.domain.service.userdetails;

import java.util.Collection;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import com.google.common.base.Function;
import com.google.common.collect.Collections2;

import am.ik.categolj2.domain.model.Role;
import am.ik.categolj2.domain.model.User;

public class Categolj2UserDetails extends
		org.springframework.security.core.userdetails.User {
	private static final long serialVersionUID = 1L;

	private final User user;

	public Categolj2UserDetails(User user) {
		super(user.getUsername(), user.getPassword(), user.isEnabled(), true,
				true, !user.isLocked(), toAuthorities(user.getRoles()));
		this.user = user;
	}

	private static Collection<? extends GrantedAuthority> toAuthorities(
			Collection<Role> roles) {
		return Collections2.transform(roles,
				new Function<Role, SimpleGrantedAuthority>() {
					@Override
					public SimpleGrantedAuthority apply(Role role) {
						return new SimpleGrantedAuthority("ROLE_"
								+ role.getRoleName());
					}
				});
	}

	public User getUser() {
		return user;
	}

}
