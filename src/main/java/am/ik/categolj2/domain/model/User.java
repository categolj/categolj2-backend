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

import java.util.Objects;
import java.util.Set;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import am.ik.categolj2.domain.validation.UserEmail;
import am.ik.categolj2.domain.validation.Username;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.springframework.util.CollectionUtils;

@Data
@EqualsAndHashCode(callSuper = false)
@AllArgsConstructor
@NoArgsConstructor
@ToString(exclude = "roles")
@Entity
@Table(name = "USER", indexes = {
        @javax.persistence.Index(columnList = "LAST_MODIFIED_DATE")
})
public class User extends AbstractAuditableEntiry<String> {
    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @NotNull
    @Username
    @Column(name = "USERNAME")
    private String username;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 256)
    @Column(name = "PASSWORD")
    private String password;
    @UserEmail
    @Basic(optional = false)
    @Column(name = "EMAIL")
    private String email;
    @Basic(optional = false)
    @NotNull
    @Column(name = "ENABLED")
    private boolean enabled;
    @Basic(optional = false)
    @NotNull
    @Column(name = "LOCKED")
    private boolean locked;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 128)
    @Column(name = "FIRST_NAME")
    private String firstName;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 128)
    @Column(name = "LAST_NAME")
    private String lastName;

    @JoinTable(name = "USER_ROLE", joinColumns = {@JoinColumn(name = "USERNAME")}, inverseJoinColumns = {@JoinColumn(name = "ROLE_ID")})
    @ManyToMany(fetch = FetchType.LAZY, cascade = {CascadeType.DETACH,
            CascadeType.MERGE, CascadeType.PERSIST, CascadeType.REFRESH})
    private Set<Role> roles;

    public User(String username) {
        this.username = username;
    }

    @Transient
    @Override
    public String getId() {
        return username;
    }

    @Transient
    @Override
    public boolean isNew() {
        return username == null;
    }

    // business methods
    @Transient
    public User lock() {
        this.locked = true;
        return this;
    }

    @Transient
    public User unlock() {
        this.locked = false;
        return this;
    }

    @Transient
    public User enable() {
        this.enabled = true;
        return this;
    }

    @Transient
    public User disable() {
        this.enabled = false;
        return this;
    }

    @Transient
    public boolean hasRole(String roleName) {
        if (CollectionUtils.isEmpty(this.getRoles())) {
            return false;
        }
        return this.getRoles().stream()
                .filter(role -> Objects.equals(roleName, role.getRoleName()))
                .count() > 0;
    }

    @Transient
    public boolean isAdmin() {
        return hasRole(Roles.ADMIN);
    }

    @Transient
    public boolean isEditor() {
        return hasRole(Roles.EDITOR);
    }
}
