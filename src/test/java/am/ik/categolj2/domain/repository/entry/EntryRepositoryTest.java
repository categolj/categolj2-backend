package am.ik.categolj2.domain.repository.entry;

import static org.junit.Assert.*;
import static org.hamcrest.CoreMatchers.*;

import java.util.Set;
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
import am.ik.categolj2.domain.model.User;
import am.ik.categolj2.domain.repository.category.CategoryRepository;
import am.ik.categolj2.domain.repository.user.UserRepository;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "classpath:test-context.xml" })
@Transactional
public class EntryRepositoryTest {

	@Inject
	EntryRepository entryRepository;

	@Inject
	UserRepository userRepository;

	@Inject
	CategoryRepository categoryRepository;

	@Test
	@Rollback(false)
	public void testSave() {
		DateTime now = new DateTime();
		String user = null;

		Entry entry = new Entry(null, "Hello World!",
				"This is a sample contents.", null, now, now, user, user, true,
				null);

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
	@Rollback(false)
	public void testFindOne() {
		Entry entry = entryRepository.findOne(1);
		System.out.println(entry);
		System.out.println(entry.getCategory());
	}

	@Test
	@Rollback(false)
	public void testFindDetail() {
		Entry entry = entryRepository.findDetails(1);
		System.out.println(entry);
		System.out.println(entry.getCategory());
	}

	@Test
	@Rollback(false)
	public void testFindDetailPublished() {
		Entry entry = entryRepository.findDetailsPublished(1);
		System.out.println(entry);
	}

	@Test
	@Rollback(false)
	public void testFindDetailsByCateogyNameAndOrder() {
		Page<Entry> entries = entryRepository
				.findPageDetailsPublishedByCateogyNameAndOrder("Programmming",
						1, new PageRequest(0, 10));
		System.out.println(entries.getContent());
	}

	@Test
	@Rollback(false)
	public void testFindDetailsPublishedOrderByLastModifiedDateDesc() {
		Page<Entry> entries = entryRepository
				.findPageDetailsPublishedOrderByLastModifiedDateDesc(new PageRequest(
						0, 10));
		System.out.println(entries.getContent());
		System.out.println(entries.getTotalElements());
	}

	@Test
	@Rollback(false)
	public void testSearch() {
		System.out.println(entryRepository.serachPageByTerm("hello",
				new PageRequest(0, 10)).getContent());
	}
}
