package am.ik.categolj2.domain.model;

import java.util.*;
import java.util.stream.Collectors;

import lombok.Data;

import com.google.common.base.Function;
import com.google.common.base.Splitter;

@Data
public class Categories {
    public static final String SEPARATOR = "::";

    private final List<Category> categories;

    private static final Function<Category, String> CATEGORY_TO_STRING = new Function<Category, String>() {
        @Override
        public String apply(Category category) {
            return category.getCategoryName();
        }
    };

    public static String toString(Categories categories) {
        return toString(categories.getCategories());
    }

    public static String toString(List<Category> categories) {
        return categories.stream()
                .map(Category::getCategoryName)
                .collect(Collectors.joining(Categories.SEPARATOR));
    }

    public static Categories fromCategory(String categoryString) {
        Iterable<String> names = Splitter.on(Categories.SEPARATOR)
                .omitEmptyStrings().split(categoryString);
        int i = 1;
        List<Category> categories = new ArrayList<>();
        for (String name : names) {
            categories.add(new Category(null, i++, name));
        }
        return new Categories(categories);
    }

    public Categories applyEntryId(Integer entryId) {
        categories.forEach(c -> c.getCategoryPK().setEntryId(entryId));
        return this;
    }
}
