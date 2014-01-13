package am.ik.categolj2.api.loginhistory;

import am.ik.categolj2.api.Categolj2Headers;
import am.ik.categolj2.domain.model.LoginHistory;
import am.ik.categolj2.domain.service.loginhistory.LoginHistoryService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.inject.Inject;

@Controller
@RequestMapping("loginhistories")
public class LoginHistoryRestController {

    @Inject
    LoginHistoryService loginHistoryService;

    @RequestMapping(method = RequestMethod.GET, headers = Categolj2Headers.X_ADMIN)
    @ResponseBody
    public Page<LoginHistory> getLoginHistoriesInAdmin(@PageableDefault Pageable pageable) {
        return loginHistoryService.findPage(pageable);
    }
}
