package am.ik.categolj2.domain.service.category;

import am.ik.categolj2.domain.model.Categories;
import am.ik.categolj2.domain.model.Category;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.stream.Collectors;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CategoryDto {
    private List<String> categoryName;

    public CategoryDto(Categories categories) {
        this.categoryName = categories.getCategories().stream()
                .map(Category::getCategoryName)
                .collect(Collectors.toList());
    }
}
