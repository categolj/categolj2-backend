package am.ik.categolj2.api.tag;

import am.ik.categolj2.api.ApiVersion;
import am.ik.categolj2.domain.service.tag.TagService;
import org.dozer.Mapper;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.inject.Inject;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@RestController
@RequestMapping("api/" + ApiVersion.CURRENT_VERSION)
public class TagRestController {
    @Inject
    Mapper beanMapper;
    @Inject
    TagService tagService;

    @RequestMapping(method = RequestMethod.GET, value = "tags")
    List<TagResource> getTags() {
        return tagService.findAll().stream()
                .map(tag -> beanMapper.map(tag, TagResource.class))
                .collect(Collectors.toList());
    }

    @RequestMapping(method = RequestMethod.GET, value = "tags", params = "tagName")
    List<TagResource> searchTags(@RequestParam("tagName") Optional<String> tagNameOpt) {
        return tagNameOpt
                .map(tagName -> {
                    if (tagName.length() > 1) {
                        return tagService.findLikeTagName(tagName).stream()
                                .map(tag -> beanMapper.map(tag, TagResource.class))
                                .collect(Collectors.toList());
                    } else {
                        return Collections.<TagResource>emptyList();
                    }
                })
                .orElseGet(this::getTags);
    }
}