package am.ik.categolj2.domain.service.category;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import javax.inject.Inject;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "classpath:test-context.xml" })
public class CategoryServiceImplTest {
    @Inject
    CategoryService categoryService;

    @Test
    public void testFindAll() throws Exception {
        System.out.println(categoryService.findAll());
    }

    @Test
    public void testSearch() throws Exception {
        System.out.println(categoryService.search("java"));
    }
}
