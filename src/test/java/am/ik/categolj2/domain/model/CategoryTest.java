package am.ik.categolj2.domain.model;

import static org.junit.Assert.*;
import static org.hamcrest.CoreMatchers.*;

import java.util.Iterator;
import java.util.SortedSet;
import java.util.TreeSet;

import org.dozer.Mapper;
import org.dozer.spring.DozerBeanMapperFactoryBean;
import org.junit.Test;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;

public class CategoryTest {

	@Test
	public void testEquals() {
		Category c1 = new Category(1, 1, "Programmming");
		Category c2 = new Category(1, 1, "Programmming");
		Category c3 = new Category(1, 1, "Prog");
		Category c4 = new Category(1, 2, "Programmming");
		Category d1 = new Category(2, 1, "Programmming");
		assertThat(c1, is(c2));
		assertThat(c1, is(not(c3)));
		assertThat(c1, is(not(c4)));
		assertThat(c1, is(not(d1)));
	}

	@Test
	public void testInTreeSet() {
		Category c1 = new Category(1, 1, "Programmming");
		Category c2 = new Category(1, 2, "Java");
		Category c3 = new Category(1, 3, "org");
		Category c4 = new Category(2, 4, "springframework");
		Category c5 = new Category(1, 5, "core");
		SortedSet<Category> categories = new TreeSet<Category>();

		// added randomly
		categories.add(c3);
		categories.add(c1);
		categories.add(c5);
		categories.add(c2);
		categories.add(c4);

		Iterator<Category> iterator = categories.iterator();

		assertThat(iterator.next(), is(c1));
		assertThat(iterator.next(), is(c2));
		assertThat(iterator.next(), is(c3));
		assertThat(iterator.next(), is(c4));
		assertThat(iterator.next(), is(c5));
	}
}
