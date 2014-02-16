/*
 * Copyright (C) 2014 Toshiaki Maki
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND,
 * either express or implied. See the License for the specific language
 * governing permissions and limitations under the License.
 */
package am.ik.categolj2.domain.service.entry;

import static org.junit.Assert.*;
import static org.hamcrest.CoreMatchers.*;

import java.util.Arrays;
import java.util.List;

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

import am.ik.categolj2.domain.model.Category;
import am.ik.categolj2.domain.model.Entry;
import am.ik.categolj2.domain.model.EntryHistory;
import am.ik.categolj2.domain.repository.category.CategoryRepository;
import am.ik.categolj2.domain.repository.entry.EntryRepository;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "classpath:test-context.xml" })
public class EntryServiceImplTest {

	@Inject
	EntryService entryService;
	@Inject
	EntryRepository entryRepository;

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
	@Transactional
	public void testCreate() {
		Entry entry = new Entry();
		entry.setTitle("My first entry");
		entry.setContents("Hi, This is duke.");
		entry.setFormat("md");
		entry.setPublished(true);
		entry.setCreatedDate(new DateTime(1984, 4, 12, 0, 0));
		List<Category> category = Arrays.asList(new Category(null, 1, "aaa"),
				new Category(null, 2, "bbb"), new Category(null, 3, "ccc"));
		Entry created = entryService.create(entry, category);
		EntryServiceImplTest.entryId = created.getEntryId();
	}

	@Test
	@Rollback(false)
	@Transactional
	public void testUpdate() {
		System.out.println("update test");
		Integer entryId = EntryServiceImplTest.entryId;

		Entry entry = new Entry();
		entry.setTitle("update test");
		entry.setContents("This contents is updated!");
		entry.setFormat("md");
		entry.setPublished(true);

		List<Category> category = Lists.newArrayList(new Category(entryId, 1,
				"xx"), new Category(entryId, 2, "yy"), new Category(entryId, 3,
				"zz"));
		entry.setCategory(category);

		System.out.println(entry.getCategory().size() + " count: "
				+ entry.getCategory());
		System.out.println("do update");
		Entry updated = entryService.update(entryId, entry, true, true);
		System.out.println(updated);

		assertThat(updated.getEntryId(), is(entryId));
		assertThat(updated.getTitle(), is("update test"));
		assertThat(updated.getContents(), is("This contents is updated!"));
		assertThat(updated.getFormat(), is("md"));
		assertThat(updated.isPublished(), is(true));
		assertThat(updated.getCategory(), is(category));

	}

	// @Test
	@Rollback(false)
	@Transactional
	public void testUpdate_reduce_category() {
		System.out.println("update test 2");
		Integer entryId = EntryServiceImplTest.entryId;

		Entry entry = new Entry();
		entry.setTitle("update test 2");
		entry.setContents("This contents is updated twice!");
		entry.setFormat("html");
		entry.setPublished(true);

		List<Category> category = Lists.newArrayList(new Category(entryId, 1,
				"xx"), new Category(entryId, 2, "foo"));
		entry.setCategory(category);

		System.out.println(entry.getCategory().size() + " count: "
				+ entry.getCategory());
		System.out.println("do update");
		Entry updated = entryService.update(entryId, entry, true, true);
		System.out.println(updated);

		assertThat(updated.getEntryId(), is(entryId));
		assertThat(updated.getTitle(), is("update test 2"));
		assertThat(updated.getContents(), is("This contents is updated twice!"));
		assertThat(updated.getFormat(), is("html"));
		assertThat(updated.isPublished(), is(true));
		assertThat(updated.getCategory(), is(category));

	}

	@Test
	@Rollback(false)
	@Transactional
	public void testDelete() {
		Entry entry = entryService.findOne(EntryServiceImplTest.entryId);
		for (EntryHistory h : entry.getHistories()) {
			System.out.println(h);
		}
		entryService.delete(EntryServiceImplTest.entryId);
		assertThat(entryRepository.findOne(EntryServiceImplTest.entryId),
				is(nullValue()));
	}

}
