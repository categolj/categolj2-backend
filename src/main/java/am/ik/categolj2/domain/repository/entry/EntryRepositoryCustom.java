package am.ik.categolj2.domain.repository.entry;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import am.ik.categolj2.domain.model.Entry;

public interface EntryRepositoryCustom {
	Page<Entry> serachPageByTerm(String term, Pageable pageable);
}
