package am.ik.categolj2.domain.model;

import org.springframework.util.Assert;
import org.springframework.util.CollectionUtils;

import java.util.Objects;

public class Roles {
    public static String ADMIN = "ADMIN";
    public static String EDITOR = "ADMIN";

    public static boolean hasRole(User user, String roleName) {
        Assert.notNull(user, "user must not be null");

        if (CollectionUtils.isEmpty(user.getRoles())) {
            return false;
        }
        return user.getRoles().stream()
                .filter(role -> Objects.equals(roleName, role.getRoleName()))
                .count() > 0;
    }

    public static boolean isAdmin(User user) {
        return hasRole(user, ADMIN);
    }

    public static boolean isEditor(User user) {
        return hasRole(user, EDITOR);
    }
}
