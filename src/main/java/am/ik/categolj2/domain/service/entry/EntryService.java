package am.ik.categolj2.domain.service.entry;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import am.ik.categolj2.domain.model.Category;
import am.ik.categolj2.domain.model.Entry;

public interface EntryService {
	Entry findOne(Integer entryId);

	Entry findOnePublished(Integer entryId);

	Page<Entry> findPage(Pageable pageable);

	Page<Entry> findPagePublished(Pageable pageable);

	List<Entry> findAllPublishedUpdatedRecently();

	Page<Entry> findPagePublishedByCateogryNameAndCategoryOrder(
			String categoryName, Integer categoryOrder, Pageable pageable);

    Page<Entry> searchPageByKeyword(String keyword, Pageable pageable);

	Entry create(Entry entry, List<Category> category);

	Entry update(Integer entryId, Entry updatedEntry,
			boolean updateLastModifiedDate, boolean saveInHistory);

	void delete(Integer entryId);

}
