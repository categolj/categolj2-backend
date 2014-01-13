package am.ik.categolj2.domain.service.loginhistory;

import am.ik.categolj2.domain.model.LoginHistory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface LoginHistoryService {
    void save(LoginHistory loginHistory);

    Page<LoginHistory> findPage(Pageable pageable);
}
