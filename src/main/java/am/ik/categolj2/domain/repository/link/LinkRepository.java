package am.ik.categolj2.domain.repository.link;

import org.springframework.data.jpa.repository.JpaRepository;

import am.ik.categolj2.domain.model.Link;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface LinkRepository extends JpaRepository<Link, String> {
    @Query("SELECT x FROM Link x ORDER BY x.lastModifiedDate DESC")
    List<Link> findAllOrderByLastModifiedDateDesc();
}
