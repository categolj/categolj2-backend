package am.ik.categolj2.api.user;

import am.ik.categolj2.api.Categolj2Headers;
import am.ik.categolj2.domain.model.User;
import am.ik.categolj2.domain.service.user.UserService;
import com.google.common.base.Function;
import com.google.common.collect.Collections2;
import org.dozer.Mapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.inject.Inject;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

@Controller
@RequestMapping("users")
public class UserRestController {
    @Inject
    UserService userService;

    @Inject
    Mapper beanMapper;

    @RequestMapping(method = RequestMethod.GET, headers = Categolj2Headers.X_ADMIN)
    @ResponseBody
    public Page<UserResource> getUsers(@PageableDefault(size = 200) Pageable pageable) {
        Page<User> page = userService.findPage(pageable);
        List<UserResource> userResources = new ArrayList<>();
        for (User user : page) {
            userResources.add(toResource(user));
        }
        Page<UserResource> result = new PageImpl<>(userResources, pageable, page.getTotalPages());
        return result;
    }

    @RequestMapping(value = "{username}", method = RequestMethod.GET, headers = Categolj2Headers.X_ADMIN)
    @ResponseBody
    public UserResource getUser(@PathVariable("username") String username) {
        return toResource(userService.findOne(username));
    }

    @RequestMapping(method = RequestMethod.POST, headers = Categolj2Headers.X_ADMIN)
    @ResponseBody
    public ResponseEntity<UserResource> postUsers(@Validated @RequestBody UserResource userResource) {
        User created = userService.create(fromResource(userResource), userResource.getPassword());
        return new ResponseEntity<>(toResource(created), HttpStatus.CREATED);
    }

    @RequestMapping(value = "{username}", method = RequestMethod.PUT, headers = Categolj2Headers.X_ADMIN)
    @ResponseBody
    public ResponseEntity<UserResource> putUsers(@PathVariable("username") String username, @Validated @RequestBody UserResource userResource) {
        User updated = userService.update(username, fromResource(userResource), userResource.getPassword());
        return new ResponseEntity<>(toResource(updated), HttpStatus.OK);
    }

    @RequestMapping(value = "{username}", method = RequestMethod.DELETE, headers = Categolj2Headers.X_ADMIN)
    @ResponseBody
    public ResponseEntity<Void> postUsers(@PathVariable("username") String username) {
        userService.delete(username);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    User fromResource(UserResource userResource) {
        User user = beanMapper.map(userResource, User.class);
        return user;
    }

    UserResource toResource(User user) {
        UserResource userResource = beanMapper.map(user, UserResource.class);
        return userResource;
    }
}
