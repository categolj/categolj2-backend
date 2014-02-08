package am.ik.categolj2.domain.service.category;

import am.ik.categolj2.domain.model.Categories;
import am.ik.categolj2.domain.repository.category.CategoryRepository;
import org.springframework.stereotype.Service;
import org.terasoluna.gfw.common.query.QueryEscapeUtils;

import javax.inject.Inject;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class CategoryServiceImpl implements CategoryService {
    @Inject
    CategoryRepository categoryRepository;

    @Override
    public List<CategoryDto> findAll() {
        List<String> categoryStrings = categoryRepository.findAllConcatenatedCategory();
        return categoryStrings.stream()
                .map(string -> new CategoryDto(Categories.fromCategory(string)))
                .collect(Collectors.toList());
    }

    @Override
    public List<CategoryDto> search(String keyword) {
        String categoryName = QueryEscapeUtils.toContainingCondition(keyword);
        List<String> categoryStrings = categoryRepository.findConcatenatedCategoryLikeCategoryName(categoryName);
        return categoryStrings.stream()
                .map(string -> new CategoryDto(Categories.fromCategory(string)))
                .collect(Collectors.toList());
    }
}
