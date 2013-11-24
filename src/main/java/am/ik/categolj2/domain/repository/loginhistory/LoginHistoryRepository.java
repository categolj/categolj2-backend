package am.ik.categolj2.domain.repository.loginhistory;

import org.springframework.data.jpa.repository.JpaRepository;

import am.ik.categolj2.domain.model.LoginHistory;

public interface LoginHistoryRepository extends
		JpaRepository<LoginHistory, String> {

}
