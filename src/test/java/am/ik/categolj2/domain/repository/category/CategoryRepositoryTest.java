package am.ik.categolj2.domain.repository.category;

import static org.junit.Assert.*;


import javax.inject.Inject;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;

import am.ik.categolj2.domain.repository.entry.EntryRepository;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "classpath:test-context.xml" })
@Transactional
public class CategoryRepositoryTest {
	@Inject
	CategoryRepository categoryRepository;

	@Inject
	EntryRepository entryRepository;

	@Test
	public void test() {
		System.out.println(categoryRepository.findAllConcatenatedCategory());
	}

}
