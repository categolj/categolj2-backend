package am.ik.categolj2.api.user;

import am.ik.categolj2.api.Categolj2Headers;
import am.ik.categolj2.domain.model.User;
import am.ik.categolj2.domain.service.user.UserService;
import org.dozer.Mapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.inject.Inject;

@Controller
@RequestMapping("users")
public class UserRestController {
    @Inject
    UserService userService;

    @Inject
    Mapper beanMapper;

    @RequestMapping(method = RequestMethod.GET, headers = Categolj2Headers.X_ADMIN)
    @ResponseBody
    public Page<User> getUsers(@PageableDefault Pageable pageable) {
        return userService.findPage(pageable);
    }

    @RequestMapping(value = "{username}", method = RequestMethod.GET, headers = Categolj2Headers.X_ADMIN)
    @ResponseBody
    public User getUser(@PathVariable("username") String username) {
        return userService.findOne(username);
    }

    @RequestMapping(method = RequestMethod.POST, headers = Categolj2Headers.X_ADMIN)
    @ResponseBody
    public ResponseEntity<User> postUsers(@Validated User user) {
        User created = userService.create(user, user.getPassword());
        return new ResponseEntity<>(created, HttpStatus.CREATED);
    }

    @RequestMapping(value = "{username}", method = RequestMethod.PUT, headers = Categolj2Headers.X_ADMIN)
    @ResponseBody
    public ResponseEntity<User> postUsers(@PathVariable("username") String username, @Validated User user) {
        User updated = userService.update(username, user, user.getPassword());
        return new ResponseEntity<>(updated, HttpStatus.OK);
    }

    @RequestMapping(value = "{username}", method = RequestMethod.DELETE, headers = Categolj2Headers.X_ADMIN)
    @ResponseBody
    public ResponseEntity<Void> postUsers(@PathVariable("username") String username) {
        userService.delete(username);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}
