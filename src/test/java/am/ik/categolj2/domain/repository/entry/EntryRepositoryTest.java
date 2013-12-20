package am.ik.categolj2.domain.repository.entry;

import static org.junit.Assert.*;
import static org.hamcrest.CoreMatchers.*;

import java.util.SortedSet;
import java.util.TreeSet;

import javax.inject.Inject;

import org.joda.time.DateTime;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;

import am.ik.categolj2.domain.model.Category;
import am.ik.categolj2.domain.model.Entry;
import am.ik.categolj2.domain.repository.category.CategoryRepository;
import am.ik.categolj2.domain.repository.user.UserRepository;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "classpath:test-context.xml" })
public class EntryRepositoryTest {

	@Inject
	EntryRepository entryRepository;

	@Inject
	UserRepository userRepository;

	@Inject
	CategoryRepository categoryRepository;

	@Test
	@Rollback(false)
	@Transactional
	public void testSave() {
		DateTime now = new DateTime();
		String user = null;

		Entry entry = new Entry(null, "hoge!", "日本語のサンプルです", "html", null,
				true, null);

		entry.setCreatedBy(user);
		entry.setCreatedDate(now);
		entry.setLastModifiedBy(user);
		entry.setLastModifiedDate(now);

		entryRepository.saveAndFlush(entry);
		assertThat(entry.getEntryId(), is(notNullValue()));

		System.out.println(entry);

		int order = 1;
		Category c1 = new Category(entry.getEntryId(), order++, "Programmming");
		Category c2 = new Category(entry.getEntryId(), order++, "Java");
		Category c3 = new Category(entry.getEntryId(), order++, "org");
		Category c4 = new Category(entry.getEntryId(), order++,
				"springframework");
		Category c5 = new Category(entry.getEntryId(), order++, "core");
		SortedSet<Category> category = new TreeSet<Category>();

		// added randomly
		category.add(c1);
		category.add(c2);
		category.add(c3);
		category.add(c4);
		category.add(c5);

		categoryRepository.save(category);
	}

	@Test
	public void testFindOne() {
		Entry entry = entryRepository.findOne(1);
//		System.out.println(entry);
//		if (entry != null) {
//			System.out.println(entry.getCategory());
//		}
        assertThat(entry, is(notNullValue()));
	}

	@Test
	public void testFindDetail() {
		Entry entry = entryRepository.findDetails(1);
		if (entry != null) {
			System.out.println(entry);
			System.out.println(entry.getCategory());
		}
	}

	@Test
	public void testFindDetailPublished() {
		Entry entry = entryRepository.findDetailsPublished(1);
		System.out.println(entry);
	}

	@Test
	public void testFindDetailsByCateogyNameAndOrder() {
		Page<Entry> entries = entryRepository
				.findPageDetailsPublishedByCateogyNameAndOrder("Programmming",
						1, new PageRequest(0, 10));
		System.out.println(entries.getContent());
	}

	@Test
	public void testFindDetailsPublishedOrderByLastModifiedDateDesc() {
		Page<Entry> entries = entryRepository
				.findPageDetailsPublishedOrderByLastModifiedDateDesc(new PageRequest(
						0, 10));
		System.out.println(entries.getContent());
		System.out.println(entries.getTotalElements());
	}

	@Test
	@Transactional
	public void testSearch() {
		System.out.println(entryRepository.serachPageByKeyword("日本語",
				new PageRequest(0, 10)).getContent());
	}
}
