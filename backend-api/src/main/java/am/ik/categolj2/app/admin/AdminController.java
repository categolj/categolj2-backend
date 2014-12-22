package am.ik.categolj2.app.admin;

import am.ik.categolj2.app.Categolj2Cookies;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

@Controller
public class AdminController {
    @RequestMapping("admin")
    String admin(@CookieValue(value = Categolj2Cookies.ACCESS_TOKEN_VALUE_COOKIE, required = false) String accessToken) {
        if (accessToken == null || accessToken.isEmpty()) {
            return "redirect:/login";
        }
        if ("http".equalsIgnoreCase(ServletUriComponentsBuilder.fromCurrentRequest().build().getScheme())) {
            return "redirect:/login";
        }
        return "forward:/backend-ui.html";
    }
}
