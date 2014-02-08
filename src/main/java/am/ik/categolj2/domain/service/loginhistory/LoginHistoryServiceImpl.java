package am.ik.categolj2.domain.service.loginhistory;

import javax.inject.Inject;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import am.ik.categolj2.domain.model.LoginHistory;
import am.ik.categolj2.domain.repository.loginhistory.LoginHistoryRepository;

@Service
public class LoginHistoryServiceImpl implements LoginHistoryService {
    @Inject
    LoginHistoryRepository loginHistoryRepository;

    @Override
    @Transactional
    public void save(LoginHistory loginHistory) {
        loginHistoryRepository.save(loginHistory);
    }

    @Override
    public Page<LoginHistory> findPage(Pageable pageable) {
        return loginHistoryRepository.findPageOrderByLoginDateDesc(pageable);
    }
}
