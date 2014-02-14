package am.ik.categolj2.domain.repository.entry;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import am.ik.categolj2.domain.model.Entry;

public interface EntryRepository extends JpaRepository<Entry, Integer>,
        EntryRepositoryCustom {
    @Query(value = "SELECT DISTINCT x FROM Entry x LEFT JOIN FETCH x.category ORDER BY x.lastModifiedDate DESC", countQuery = "SELECT COUNT(x) FROM Entry x")
    @Deprecated
    Page<Entry> findPageDetailsOrderByLastModifiedDateDesc(Pageable pageable);

    @Query(value = "SELECT x FROM Entry x ORDER BY x.lastModifiedDate DESC", countQuery = "SELECT COUNT(x) FROM Entry x")
    Page<Entry> findPageOrderByLastModifiedDateDesc(Pageable pageable);

    @Query(value = "SELECT DISTINCT x FROM Entry x LEFT JOIN FETCH x.category WHERE x.published = true ORDER BY x.lastModifiedDate DESC", countQuery = "SELECT COUNT(x) FROM Entry x WHERE x.published = true")
    @Deprecated
    Page<Entry> findPageDetailsPublishedOrderByLastModifiedDateDesc(
            Pageable pageable);

    @Query(value = "SELECT x FROM Entry x WHERE x.published = true ORDER BY x.lastModifiedDate DESC", countQuery = "SELECT COUNT(x) FROM Entry x WHERE x.published = true")
    Page<Entry> findPagePublishedOrderByLastModifiedDateDesc(
            Pageable pageable);

    @Query(value = "SELECT DISTINCT x FROM Entry x LEFT JOIN FETCH x.category WHERE x.published = true ORDER BY x.lastModifiedDate DESC", countQuery = "SELECT COUNT(x) FROM Entry x WHERE x.published = true")
    @Deprecated
    List<Entry> findAllDetailsPublishedOrderByLastModifiedDateDesc(
            Pageable pageable);

    @Query(value = "SELECT x FROM Entry x WHERE x.published = true ORDER BY x.lastModifiedDate DESC", countQuery = "SELECT COUNT(x) FROM Entry x WHERE x.published = true")
    List<Entry> findAllPublishedOrderByLastModifiedDateDesc(
            Pageable pageable);

    @Query("SELECT x FROM Entry x LEFT JOIN FETCH x.category WHERE x.entryId = :entryId")
    Entry findDetails(@Param("entryId") Integer entryId);

    @Query("SELECT x FROM Entry x LEFT JOIN FETCH x.category WHERE x.entryId = :entryId AND x.published = true")
    Entry findDetailsPublished(@Param("entryId") Integer entryId);

    @Query("SELECT DISTINCT x FROM Entry x LEFT JOIN FETCH x.category WHERE x.entryId IN (:ids) ORDER BY x.lastModifiedDate DESC")
    List<Entry> findAllDetailsByEntryIdIn(@Param("ids") List<Integer> ids);

    @Query(value = "SELECT DISTINCT x FROM Entry x"
            + " WHERE x.entryId IN"
            + "  (SELECT c.categoryPK.entryId FROM Category c WHERE c.categoryName = :categoryName AND c.categoryPK.categoryOrder = :categoryOrder)"
            + "  AND x.published = true" + " ORDER BY x.lastModifiedDate DESC", countQuery = "SELECT COUNT(x) FROM Entry x "
            + " WHERE x.entryId IN "
            + "  (SELECT c.categoryPK.entryId FROM Category c WHERE c.categoryName = :categoryName AND c.categoryPK.categoryOrder = :categoryOrder)"
            + "  AND x.published = true")
    Page<Entry> findPageDetailsPublishedByCategoryNameAndOrder(
            @Param("categoryName") String categoryName,
            @Param("categoryOrder") Integer categoryOrder, Pageable pageable);

    @Query(value = "SELECT x FROM Entry x WHERE x.createdBy = :createdBy AND x.published = true ORDER BY x.lastModifiedDate DESC",
            countQuery = "SELECT COUNT(x) FROM Entry x WHERE x.createdBy = :createdBy AND x.published = true")
    Page<Entry> findPagePublishedByCreatedBy(
            @Param("createdBy") String createdBy, Pageable pageable);
}
