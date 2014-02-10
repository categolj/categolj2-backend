package am.ik.categolj2.infra.dozer;

import static org.junit.Assert.*;
import static org.hamcrest.CoreMatchers.*;

import java.util.Arrays;

import am.ik.categolj2.api.entry.EntryResource;
import org.dozer.DozerBeanMapper;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import com.google.common.collect.Lists;

import am.ik.categolj2.domain.model.Categories;
import am.ik.categolj2.domain.model.Category;
import am.ik.categolj2.domain.model.Entry;

public class StringToCategoryListConverterTest {
	DozerBeanMapper beanMapper = new DozerBeanMapper(
			Arrays.asList("META-INF/dozer/api-mapping.xml"));

	@Before
	public void setUp() throws Exception {
	}

	@After
	public void tearDown() throws Exception {
	}

	@Test
	public void testConvertStringToCategoryList() {
		Integer entryId = 100;
		EntryResource form = new EntryResource();
		form.setContents("aa");
		form.setTitle("hello");
		form.setCategoryString("foo::bar::zzz");
		form.setEntryId(entryId);

		Entry entry = beanMapper.map(form, Entry.class);

		assertThat(entry.getEntryId(), is(entryId));
		assertThat(entry.getContents(), is("aa"));
		assertThat(entry.getTitle(), is("hello"));
		new Categories(entry.getCategory()).applyEntryId(entryId);
		assertThat(entry.getCategory(), is(Arrays.asList(new Category(entryId,
				1, "foo"), new Category(entryId, 2, "bar"), new Category(
				entryId, 3, "zzz"))));
	}

	@Test
	public void testConvertEmptyStringToEmptyCategoryList() {
		Integer entryId = 100;
		EntryResource form = new EntryResource();
		form.setContents("aa");
		form.setTitle("hello");
		form.setCategoryString("");
		form.setEntryId(entryId);

		Entry entry = beanMapper.map(form, Entry.class);

		assertThat(entry.getEntryId(), is(entryId));
		assertThat(entry.getContents(), is("aa"));
		assertThat(entry.getTitle(), is("hello"));
		new Categories(entry.getCategory()).applyEntryId(entryId);
		assertThat(entry.getCategory().isEmpty(), is(true));
	}

	@Test
	public void testConvertCategoryListToString() {
		Integer entryId = 10;
		Entry entry = new Entry();
		entry.setContents("aa");
		entry.setTitle("hello");
		entry.setCategory(Lists.newArrayList(new Category(entryId, 1, "xx"),
				new Category(entryId, 2, "yy"), new Category(entryId, 3, "zz")));
		entry.setEntryId(entryId);

		EntryResource form = beanMapper.map(entry, EntryResource.class);

		assertThat(form.getEntryId(), is(entryId));
		assertThat(form.getContents(), is("aa"));
		assertThat(form.getTitle(), is("hello"));
		assertThat(form.getCategoryString(), is("xx::yy::zz"));
	}
}
