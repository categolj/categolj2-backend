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
