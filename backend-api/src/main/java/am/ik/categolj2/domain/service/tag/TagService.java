package am.ik.categolj2.domain.service.tag;


import am.ik.categolj2.domain.model.Tag;

import java.util.List;

public interface TagService {
    List<Tag> findAll();

    List<Tag> findLikeTagName(String tagName);
}
