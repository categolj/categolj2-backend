package am.ik.categolj2.domain.service.entry;

import java.util.List;

import javax.inject.Inject;

import org.joda.time.DateTime;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;
import org.terasoluna.gfw.common.date.DateFactory;
import org.terasoluna.gfw.common.exception.ResourceNotFoundException;

import am.ik.categolj2.domain.model.Category;
import am.ik.categolj2.domain.model.Entry;
import am.ik.categolj2.domain.repository.category.CategoryRepository;
import am.ik.categolj2.domain.repository.entry.EntryRepository;

@Service
public class EntryServiceImpl implements EntryService {
	@Inject
	protected EntryRepository entryRepository;

	@Inject
	protected CategoryRepository categoryRepository;

	@Inject
	protected DateFactory dateFactory;

	protected Pageable recentlyPageable = new PageRequest(0, 10);

	@Override
	public Entry findOne(Integer entryId) {
		Entry entry = entryRepository.findDetails(entryId);
		if (entry == null) {
			throw new ResourceNotFoundException(
					"The request entry is not found. (entryId=" + entryId + ")");
		}
		return entry;
	}

	@Override
	public Entry findOnePublished(Integer entryId) {
		Entry entry = entryRepository.findDetailsPublished(entryId);
		if (entry == null) {
			throw new ResourceNotFoundException(
					"The request entry is not found. (entryId=" + entryId + ")");
		}
		return entry;
	}

	@Override
	public Page<Entry> findPage(Pageable pageable) {
		return entryRepository
				.findPageDetailsOrderByLastModifiedDateDesc(pageable);
	}

	@Override
	public Page<Entry> findPagePublished(Pageable pageable) {
		return entryRepository
				.findPageDetailsPublishedOrderByLastModifiedDateDesc(pageable);
	}

	@Override
	public List<Entry> findAllPublishedUpdatedRecently() {
		return entryRepository
				.findAllDetailsPublishedOrderByLastModifiedDateDesc(recentlyPageable);
	}

	@Override
	public Page<Entry> findPagePublishedByCateogryNameAndCategoryOrder(
			String categoryName, Integer categoryOrder, Pageable pageable) {
		return entryRepository.findPageDetailsPublishedByCateogyNameAndOrder(
				categoryName, categoryOrder, pageable);
	}

	@Override
	@Transactional
	public Entry create(Entry entry, List<Category> category) {
		Assert.notNull(entry, "entry must not be null");
		Assert.isNull(entry.getCategory(), "entry.category must be null");
		Assert.notEmpty(category, "category must not be null or empty");

		DateTime now = dateFactory.newDateTime();
		entry.setCreatedDate(now);
		entry.setLastModifiedDate(now);
		// createdBy,lastModifiedBy are set by AuditingEntityListener
		entryRepository.saveAndFlush(entry);
		Integer entryId = entry.getEntryId();
		for (Category c : category) {
			c.getCategoryPK().setEntryId(entryId);
		}
		entry.setCategory(category);
		return entry;
	}

	@Override
	@Transactional
	public Entry update(Entry entry, boolean updateLastModifiedDate) {
		Assert.notNull(entry, "entry must not be null");
		if (updateLastModifiedDate) {
			DateTime now = dateFactory.newDateTime();
			entry.setLastModifiedDate(now);
		}
		entryRepository.save(entry);
		return entry;
	}

	@Override
	@Transactional
	public void delete(Integer entryId) {
		Entry entry = findOne(entryId);
		entryRepository.delete(entry);
	}
}
