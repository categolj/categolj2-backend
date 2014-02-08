package am.ik.categolj2.domain.repository.category;

import java.util.Collection;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import am.ik.categolj2.domain.model.Category;
import am.ik.categolj2.domain.model.CategoryPK;

public interface CategoryRepository extends JpaRepository<Category, CategoryPK> {

    @Query(value = "SELECT DISTINCT GROUP_CONCAT(DISTINCT category_name ORDER BY category_order ASC SEPARATOR '::') category FROM category GROUP BY entry_id ORDER BY category", nativeQuery = true)
    List<String> findAllConcatenatedCategory();

    @Query(value = "SELECT DISTINCT GROUP_CONCAT(DISTINCT category_name ORDER BY category_order ASC SEPARATOR '::') category FROM category GROUP BY entry_id HAVING lower(category) LIKE lower(:categoryName) ORDER BY category", nativeQuery = true)
    List<String> findConcatenatedCategoryLikeCategoryName(@Param("categoryName") String categoryName);

    @Query("SELECT x FROM Category x WHERE x.entry.entryId IN (:entryIds)")
    List<Category> findByEntryIds(@Param("entryIds") List<Integer> entryIds);
}
