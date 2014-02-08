package am.ik.categolj2.domain.repository.loginhistory;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import am.ik.categolj2.domain.model.LoginHistory;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface LoginHistoryRepository extends
        JpaRepository<LoginHistory, String> {
    @Query("SELECT x FROM LoginHistory x ORDER BY x.loginDate DESC")
    Page<LoginHistory> findPageOrderByLoginDateDesc(Pageable pageable);
}
