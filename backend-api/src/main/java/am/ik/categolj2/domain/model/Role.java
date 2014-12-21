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

import java.util.Set;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
import javax.persistence.Table;
import javax.persistence.Transient;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;
import com.fasterxml.jackson.annotation.JsonIgnore;

@Data
@AllArgsConstructor
@NoArgsConstructor
@ToString(exclude = "users")
@EqualsAndHashCode(exclude = "users", callSuper = false)
@Entity
@Table(name = "ROLE")
public class Role extends AbstractAuditableEntiry<Integer> {
	private static final long serialVersionUID = 4518188757127538670L;
	@Id
	@GeneratedValue
	@Basic(optional = false)
	@Column(name = "ROLE_ID")
	private Integer roleId;
	@Basic(optional = false)
	@NotNull
	@Size(min = 1, max = 25)
	@Column(name = "ROLE_NAME", unique = true)
	private String roleName;
	@ManyToMany(mappedBy = "roles", fetch = FetchType.LAZY)
    @JsonIgnore
	private Set<User> users;

	public Role(Integer roleId) {
		this.roleId = roleId;
	}

	@Override
	@Transient
	public Integer getId() {
		return roleId;
	}

	@Override
	@Transient
	public boolean isNew() {
		return roleId == null;
	}
}
