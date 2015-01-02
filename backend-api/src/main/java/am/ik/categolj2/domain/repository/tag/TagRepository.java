package am.ik.categolj2.domain.repository.tag;

import am.ik.categolj2.domain.model.Tag;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface TagRepository extends JpaRepository<Tag, String> {
    @Query("SELECT NEW am.ik.categolj2.domain.repository.tag.TagAndEntryId(x, e.entryId) FROM Tag x INNER JOIN x.entries e WHERE e.entryId IN (:entryIds) ORDER BY x.tagName")
    List<TagAndEntryId> findByEntryIds(@Param("entryIds") List<Integer> entryIds);

    @Query("SELECT COUNT(y) FROM Tag x INNER JOIN x.entries y WHERE x.tagName = :tagName")
    long countByTagName(@Param("tagName") String tagName);

    @Query("SELECT x FROM Tag x ORDER BY x.tagName")
    List<Tag> findOrderByTagNameAsc();

    List<Tag> findByTagNameStartsWithOrderByTagNameAsc(String tagName);
}
