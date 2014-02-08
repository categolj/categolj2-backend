package am.ik.categolj2.domain.service.category;

import java.util.List;

public interface CategoryService {
    List<CategoryDto> findAll();

    List<CategoryDto> search(String keyword);
}
