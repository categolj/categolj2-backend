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
package am.ik.categolj2.domain.service.user;

import am.ik.categolj2.core.message.MessageKeys;
import am.ik.categolj2.domain.model.Role;
import am.ik.categolj2.domain.model.User;
import am.ik.categolj2.domain.repository.role.RoleRepository;
import am.ik.categolj2.domain.repository.user.UserRepository;
import org.dozer.Mapper;
import org.joda.time.DateTime;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;
import org.terasoluna.gfw.common.date.DateFactory;
import org.terasoluna.gfw.common.exception.BusinessException;
import org.terasoluna.gfw.common.exception.ResourceNotFoundException;
import org.terasoluna.gfw.common.message.ResultMessages;

import javax.inject.Inject;
import java.util.HashSet;
import java.util.Set;

@Service
public class UserServiceImpl implements UserService {
    @Inject
    UserRepository userRepository;
    @Inject
    PasswordEncoder passwordEncoder;
    @Inject
    Mapper beanMapper;
    @Inject
    DateFactory dateFactory;
    @Inject
    RoleRepository roleRepository;

    @Override
    public User findOne(String username) {
        User user = userRepository.findDetails(username);
        if (user == null) {
            ResultMessages messages = ResultMessages.error()
                    .add(MessageKeys.E_CT_US_8101, username);
            throw new ResourceNotFoundException(messages);
        }
        return user;
    }

    @Override
    public User findOneByUsernameOrEmail(String usernameOrEmail) {
        User user = userRepository.findOneByEmail(usernameOrEmail);
        if (user == null) {
            user = userRepository.findDetails(usernameOrEmail);
        }
        if (user == null) {
            ResultMessages messages = ResultMessages.error()
                    .add(MessageKeys.E_CT_US_8102, usernameOrEmail);
            throw new ResourceNotFoundException(messages);
        }
        return user;
    }

    @Override
    public Page<User> findPage(Pageable pageable) {
        return userRepository.findAll(pageable);
    }

    @Transactional
    @Override
    public User create(User user, String rawPassword) {
        Assert.notNull(user, "user must not be null");

        String username = user.getUsername();
        if (userRepository.exists(username)) {
            ResultMessages messages = ResultMessages.error()
                    .add(MessageKeys.E_CT_US_8103, username);
            throw new BusinessException(messages);
        }

        String email = user.getEmail();
        if (userRepository.countByEmail(email) > 0) {
            ResultMessages messages = ResultMessages.error()
                    .add(MessageKeys.E_CT_US_8104, email);
            throw new BusinessException(messages);
        }

        // check roles
        checkRoles(user);

        String encodedPassword = passwordEncoder.encode(rawPassword);
        user.setPassword(encodedPassword);
        DateTime now = dateFactory.newDateTime();
        user.setCreatedDate(now);
        user.setLastModifiedDate(now);
        // createdBy,lastModifiedBy are set by AuditingEntityListener

        userRepository.save(user);
        return user;
    }

    @Transactional
    @Override
    public User update(String username, User updatedUser,
                       String updatedRawPassword) {
        Assert.notNull(updatedUser, "user must not be null");

        preUpdate(username, updatedUser);

        // encode raw password
        String encodedPassword = passwordEncoder.encode(updatedRawPassword);
        updatedUser.setPassword(encodedPassword);

        return doUpdate(username, updatedUser);
    }

    @Transactional
    @Override
    public User updateWithoutPassword(String username, User updatedUser) {
        Assert.notNull(updatedUser, "user must not be null");
        Assert.notNull(updatedUser.getPassword(), "(old) password must not be null");

        preUpdate(username, updatedUser);
        return doUpdate(username, updatedUser);
    }

    private void preUpdate(String username, User updatedUser) {
        String email = updatedUser.getEmail();

        // check roles
        checkRoles(updatedUser);

        // check the given user is existing
        if (!userRepository.exists(username)) {
            ResultMessages messages = ResultMessages.error()
                    .add(MessageKeys.E_CT_US_8105, username);
            throw new BusinessException(messages);
        }

        // check the given email is not used by others
        long countUsingEmail = userRepository.countByEmailOtherThanMe(email, username);
        if (countUsingEmail > 0) {
            ResultMessages messages = ResultMessages.error()
                    .add(MessageKeys.E_CT_US_8104, email);
            throw new BusinessException(messages);
        }

        boolean notExistAdminOtherThanMe = userRepository.countActiveAdminOtherThanMe(username) == 0;
        boolean isAdmin = updatedUser.isAdmin();

        // in case updated user is not admin or removed admin role
        if (!isAdmin && notExistAdminOtherThanMe) {
            ResultMessages messages = ResultMessages.error()
                    .add(MessageKeys.E_CT_US_8106);
            throw new BusinessException(messages);
        }
        // if single admin is to be locked.
        if (isAdmin && notExistAdminOtherThanMe && updatedUser.isLocked()) {
            ResultMessages messages = ResultMessages.error()
                    .add(MessageKeys.E_CT_US_8107);
            throw new BusinessException(messages);
        }
        // if single admin is to be disabled
        if (isAdmin && notExistAdminOtherThanMe && !updatedUser.isEnabled()) {
            ResultMessages messages = ResultMessages.error()
                    .add(MessageKeys.E_CT_US_8108);
            throw new BusinessException(messages);
        }
    }

    private void checkRoles(User user) {
        Set<Role> rolesToReplace = new HashSet<>();
        Set<Role> roles = user.getRoles();
        if (roles != null) {
            for (Role role : roles) {
                Integer roleId = role.getRoleId();
                Role loadedRole = roleRepository.findOne(roleId);
                if (loadedRole == null) {
                    ResultMessages messages = ResultMessages.error()
                            .add(MessageKeys.E_CT_US_8109, roleId);
                    throw new BusinessException(messages);
                }
                rolesToReplace.add(loadedRole);
            }
        }
        user.setRoles(rolesToReplace); // replace
    }

    private User doUpdate(String username, User updatedUser) {
        DateTime now = dateFactory.newDateTime();
        updatedUser.setLastModifiedDate(now);
        // lastModifiedBy are set by AuditingEntityListener

        User user = findOne(username);
        // copy new values to user
        beanMapper.map(updatedUser, user);
        userRepository.save(user);
        return user;
    }

    @Transactional
    @Override
    public void delete(String username) {
        User user = findOne(username);

        // check admin count
        if (user.isAdmin() && userRepository.countActiveAdminOtherThanMe(username) == 0) {
            ResultMessages messages = ResultMessages.error()
                    .add(MessageKeys.E_CT_US_8106);
            throw new BusinessException(messages);
        }

        userRepository.delete(user);
    }

}
