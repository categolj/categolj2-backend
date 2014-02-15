package am.ik.categolj2.domain.service.loginhistory;

import am.ik.categolj2.domain.Categolj2AuthorizeAccesses;
import am.ik.categolj2.domain.model.LoginHistory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.prepost.PreAuthorize;

public interface LoginHistoryService {

    @PreAuthorize(Categolj2AuthorizeAccesses.AUTHORIZED)
    void save(LoginHistory loginHistory);

    @PreAuthorize(Categolj2AuthorizeAccesses.AUTHORIZED)
    Page<LoginHistory> findPage(Pageable pageable);
}
