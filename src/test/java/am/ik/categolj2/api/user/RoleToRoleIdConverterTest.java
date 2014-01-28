package am.ik.categolj2.api.user;

import am.ik.categolj2.domain.model.Role;
import am.ik.categolj2.domain.model.User;
import com.google.common.collect.Sets;
import org.dozer.Mapper;
import org.dozer.spring.DozerBeanMapperFactoryBean;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;
import static org.hamcrest.CoreMatchers.*;

import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;

import java.util.*;

public class RoleToRoleIdConverterTest {
    Mapper mapper;

    @Before
    public void setUp() throws Exception {
        DozerBeanMapperFactoryBean factoryBean = new DozerBeanMapperFactoryBean();
        factoryBean.setMappingFiles(new Resource[]{
                new ClassPathResource("META-INF/dozer/global-mapping.xml"),
                new ClassPathResource("META-INF/dozer/api-mapping.xml")});
        factoryBean.afterPropertiesSet();
        mapper = (Mapper) factoryBean.getObject();
    }

    @After
    public void tearDown() throws Exception {
    }

    @Test
    public void testConvertTo() throws Exception {
        User user = new User();
        Set<Role> roles = Sets.newHashSet(new Role(1), new Role(2));
        user.setRoles(roles);

        UserResource userResource = mapper.map(user, UserResource.class);
        assertThat(userResource.getRoles(), is(Arrays.asList(1, 2)));
    }

    @Test
    public void testConvertFrom() throws Exception {
        UserResource userResource = new UserResource();
        userResource.setRoles(Arrays.asList(1, 2));

        User user = mapper.map(userResource, User.class);
        List<Integer> roles = new ArrayList<>();
        for (Role role : user.getRoles()) {
            roles.add(role.getId());
        }
        Collections.sort(roles);
        assertThat(roles, is(userResource.getRoles()));
    }


    @Test
    public void testConvertFrom_forUpdate() throws Exception {
        UserResource userResource = new UserResource();
        userResource.setRoles(new ArrayList(Arrays.asList(1, 2)));


        User user = new User();
        Set<Role> roles = new LinkedHashSet<>();
        {
            Role role = new Role(1);
            role.setRoleName("ROLE_ADMIN");
            roles.add(role);
        }
        {
            Role role = new Role(2);
            role.setRoleName("ROLE_USER");
            roles.add(role);
        }
        user.setRoles(roles);
        System.out.println(user.getRoles().hashCode());
        mapper.map(userResource, user);
        System.out.println(user.getRoles().hashCode());
        System.out.println(user.getRoles());
    }
}
