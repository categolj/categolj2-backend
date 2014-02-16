package am.ik.categolj2.api.category;

import am.ik.categolj2.api.Categolj2Headers;
import am.ik.categolj2.domain.model.AccessLog;
import am.ik.categolj2.domain.service.accesslog.AccessLogService;
import am.ik.categolj2.domain.service.category.CategoryDto;
import am.ik.categolj2.domain.service.category.CategoryService;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.inject.Inject;
import java.util.List;

@RestController
@RequestMapping("categories")
public class CategoryRestController {
    @Inject
    CategoryService categoryService;
    @Inject
    AccessLogService accessLogService;

    @RequestMapping(method = RequestMethod.GET)
    public List<CategoryDto> getCategories(AccessLog accessLog) {
        accessLogService.save(accessLog);
        return categoryService.findAll();
    }


    @RequestMapping(method = RequestMethod.GET, params = "keyword", headers = Categolj2Headers.X_ADMIN)
    public List<CategoryDto> searchCategories(@RequestParam("keyword") String keyword) {
        return categoryService.search(keyword);
    }
}
