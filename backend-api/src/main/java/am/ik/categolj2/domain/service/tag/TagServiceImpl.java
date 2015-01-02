package am.ik.categolj2.domain.service.tag;

import am.ik.categolj2.domain.model.Tag;
import am.ik.categolj2.domain.repository.tag.TagRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.inject.Inject;
import java.util.List;

@Service
@Transactional
public class TagServiceImpl implements TagService {
    @Inject
    TagRepository tagRepository;

    @Override
    @Transactional(readOnly = true)
    public List<Tag> findAll() {
        return tagRepository.findOrderByTagNameAsc();
    }

    @Override
    @Transactional(readOnly = true)
    public List<Tag> findLikeTagName(String tagName) {
        return tagRepository.findByTagNameStartsWithOrderByTagNameAsc(tagName);
    }
}
