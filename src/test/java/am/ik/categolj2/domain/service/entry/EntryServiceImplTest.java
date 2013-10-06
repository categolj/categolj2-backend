package am.ik.categolj2.domain.service.entry;

import static org.junit.Assert.*;
import static org.hamcrest.CoreMatchers.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Set;

import javax.inject.Inject;

import org.dozer.Mapper;
import org.joda.time.DateTime;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;

import com.google.common.collect.Lists;
import com.google.common.collect.Sets;

import am.ik.categolj2.domain.model.Category;
import am.ik.categolj2.domain.model.Entry;
import am.ik.categolj2.domain.repository.category.CategoryRepository;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "classpath:test-context.xml" })
@Transactional
public class EntryServiceImplTest {

	@Inject
	EntryService entryService;

	@Inject
	CategoryRepository categoryRepository;

	@Inject
	Mapper beanMapper;

	static Integer entryId = null;

	@Before
	public void setUp() throws Exception {
	}

	@After
	public void tearDown() throws Exception {
	}

	@Test
	public void testFindOne() {
		assertThat(true, is(true));
	}

	@Test
	public void testFindOnePublished() {

	}

	@Test
	public void testFindPage() {

	}

	@Test
	public void testFindPagePublished() {

	}

	@Test
	public void testFindAllPublishedUpdatedRecently() {

	}

	@Test
	public void testFindPagePublishedByCateogryNameAndCategoryOrder() {

	}

	@Test
	@Rollback(false)
	public void testCreate() {
		Entry entry = new Entry();
		entry.setTitle("My first entry");
		entry.setContents("Hi, This is duke.");
		entry.setPublished(true);
		entry.setCreatedDate(new DateTime(1984, 4, 12, 0, 0));
		List<Category> category = Arrays.asList(new Category(null, 1, "aaa"),
				new Category(null, 2, "bbb"), new Category(null, 3, "ccc"));
		Entry created = entryService.create(entry, category);
		EntryServiceImplTest.entryId = created.getEntryId();
	}

	@Test
	@Rollback(false)
	public void testUpdate() {
		Integer entryId = EntryServiceImplTest.entryId;
		Entry entry = entryService.findOne(entryId);
		System.out.println(entry.getCategory().size() + " count: "
				+ entry.getCategory());
		entry.setTitle("update test");
		entry.setContents("This contents is updated!");

		List<Category> category = Lists.newArrayList(new Category(entryId, 1,
				"xx"), new Category(entryId, 2, "yy"), new Category(entryId, 3,
				"zz")
		// , new Category(entryId, 4, "aa")
		// , new Category(entryId, 5, "pp")
				);
		Entry dummy = new Entry();
		dummy.setCategory(category);
		beanMapper.map(dummy, entry);

		System.out.println(entry.getCategory().size() + " count: "
				+ entry.getCategory());
		Entry updated = entryService.update(entry, true);
		System.out.println(updated);
	}

	@Test
	@Rollback(false)
	public void testDelete() {
		entryService.delete(EntryServiceImplTest.entryId);
	}

}
