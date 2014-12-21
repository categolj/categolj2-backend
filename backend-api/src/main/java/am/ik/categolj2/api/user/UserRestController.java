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

import am.ik.categolj2.api.ApiVersion;
import am.ik.categolj2.api.Categolj2Headers;
import am.ik.categolj2.domain.model.User;
import am.ik.categolj2.domain.service.user.UserService;
import com.google.common.base.Strings;
import org.dozer.Mapper;
import org.springframework.beans.propertyeditors.StringTrimmerEditor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.*;

import javax.inject.Inject;
import javax.validation.groups.Default;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("api/" + ApiVersion.CURRENT_VERSION + "/users")
public class UserRestController {
    @Inject
    UserService userService;

    @Inject
    Mapper beanMapper;

    @InitBinder
    public void initBinder(WebDataBinder binder) {
        binder.registerCustomEditor(String.class, new StringTrimmerEditor(true));
    }

    @RequestMapping(method = RequestMethod.GET, headers = Categolj2Headers.X_ADMIN)
    public Page<UserResource> getUsers(@PageableDefault(size = 200) Pageable pageable) {
        Page<User> page = userService.findPage(pageable);
        List<UserResource> userResources = page.getContent().stream()
                .map(user -> beanMapper.map(user, UserResource.class))
                .collect(Collectors.toList());
        return new PageImpl<>(userResources, pageable, page.getTotalElements());
    }

    @RequestMapping(value = "me", method = RequestMethod.GET, headers = Categolj2Headers.X_ADMIN)
    public UserResource getMe(Authentication authentication) {
        return getUser(authentication.getName());
    }

    @RequestMapping(value = "{username}", method = RequestMethod.GET, headers = Categolj2Headers.X_ADMIN)
    public UserResource getUser(@PathVariable("username") String username) {
        return beanMapper.map(userService.findOne(username), UserResource.class);
    }

    @RequestMapping(method = RequestMethod.POST, headers = Categolj2Headers.X_ADMIN)
    public ResponseEntity<UserResource> postUsers(@Validated({UserResource.Create.class, Default.class}) @RequestBody UserResource userResource) {
        User created = userService.create(beanMapper.map(userResource, User.class), userResource.getPassword());
        return new ResponseEntity<>(beanMapper.map(created, UserResource.class), HttpStatus.CREATED);
    }

    @RequestMapping(value = "{username}", method = RequestMethod.PUT, headers = Categolj2Headers.X_ADMIN)
    public ResponseEntity<UserResource> putUsers(@PathVariable("username") String username, @Validated({UserResource.Update.class, Default.class}) @RequestBody UserResource userResource) {
        User updatedUser = userService.findOne(username);
        beanMapper.map(userResource, updatedUser);
        if (Strings.isNullOrEmpty(userResource.getPassword())) {
            updatedUser = userService.updateWithoutPassword(username, updatedUser);
        } else {
            updatedUser = userService.update(username, updatedUser, userResource.getPassword());
        }
        return new ResponseEntity<>(beanMapper.map(updatedUser, UserResource.class), HttpStatus.OK);
    }

    @RequestMapping(value = "{username}", method = RequestMethod.DELETE, headers = Categolj2Headers.X_ADMIN)
    public ResponseEntity<Void> postUsers(@PathVariable("username") String username) {
        userService.delete(username);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}
