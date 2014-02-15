package am.ik.categolj2.domain.service.user;

import am.ik.categolj2.domain.Categolj2AuthorizeAccesses;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import am.ik.categolj2.domain.model.User;
import org.springframework.security.access.prepost.PreAuthorize;

public interface UserService {
    User findOne(String username);

    User findOneByUsernameOrEmail(String usernameOrEmail);

    @PreAuthorize(Categolj2AuthorizeAccesses.ADMIN_ONLY)
    Page<User> findPage(Pageable pageable);

    @PreAuthorize(Categolj2AuthorizeAccesses.ADMIN_ONLY)
    User create(User user, String rawPassword);

    @PreAuthorize(Categolj2AuthorizeAccesses.AUTHORIZED)
    User update(String username, User updatedUser, String updatedRawPassword);

    @PreAuthorize(Categolj2AuthorizeAccesses.ADMIN_ONLY)
    User updateWithoutPassword(String username, User updatedUser);

    @PreAuthorize(Categolj2AuthorizeAccesses.ADMIN_ONLY)
    void delete(String username);
}
