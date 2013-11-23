package am.ik.categolj2.domain.repository.entry;

import org.springframework.data.jpa.repository.JpaRepository;

import am.ik.categolj2.domain.model.EntryHistory;

public interface EntryHistoryRepository extends JpaRepository<EntryHistory, String> {

}
