package am.ik.categolj2.domain.service.entry;

import java.util.List;

import am.ik.categolj2.domain.Categolj2AuthorizeAccesses;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import am.ik.categolj2.domain.model.Category;
import am.ik.categolj2.domain.model.Entry;
import org.springframework.security.access.prepost.PreAuthorize;

public interface EntryService {

    @PreAuthorize(Categolj2AuthorizeAccesses.AUTHORIZED)
    Entry findOne(Integer entryId);

    Entry findOnePublished(Integer entryId);

    @PreAuthorize(Categolj2AuthorizeAccesses.AUTHORIZED)
    Page<Entry> findPage(Pageable pageable);

    Page<Entry> findPagePublished(Pageable pageable);

    List<Entry> findAllPublishedUpdatedRecently();

    Page<Entry> findPagePublishedByCategoryNameAndCategoryOrder(
            String categoryName, Integer categoryOrder, Pageable pageable);

    Page<Entry> findPagePublishedByCreatedBy(String createdBy, Pageable pageable);

    @PreAuthorize(Categolj2AuthorizeAccesses.AUTHORIZED)
    Page<Entry> searchPageByKeyword(String keyword, Pageable pageable);

    Page<Entry> searchPagePublishedByKeyword(String keyword, Pageable pageable);

    @PreAuthorize(Categolj2AuthorizeAccesses.AUTHORIZED)
    Entry create(Entry entry, List<Category> category);

    @PreAuthorize(Categolj2AuthorizeAccesses.AUTHORIZED)
    Entry update(Integer entryId, Entry updatedEntry,
                 boolean updateLastModifiedDate, boolean saveInHistory);

    @PreAuthorize(Categolj2AuthorizeAccesses.AUTHORIZED)
    void delete(Integer entryId);

}
