package am.ik.categolj2.domain.service.entry;

import am.ik.categolj2.domain.model.Categories;
import am.ik.categolj2.domain.model.Category;
import am.ik.categolj2.domain.model.Entry;
import am.ik.categolj2.domain.model.EntryHistory;
import am.ik.categolj2.domain.repository.category.CategoryRepository;
import am.ik.categolj2.domain.repository.entry.EntryRepository;
import com.google.common.collect.Lists;
import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.type.TypeReference;
import org.dozer.Mapper;
import org.joda.time.DateTime;
import org.joda.time.DateTimeZone;
import org.junit.After;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;

import javax.inject.Inject;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.*;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.nullValue;
import static org.junit.Assert.assertThat;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"classpath:test-context.xml"})
public class EntryImportTest {
    @Inject
    EntryRepository entryRepository;

    @Inject
    NamedParameterJdbcTemplate jdbcTemplate;

    @Ignore
    @Test
    @Rollback(false)
    @Transactional
    public void testCreate() throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        try (InputStream stream = ClassLoader.getSystemResourceAsStream("entry.json");
             BufferedReader reader = new BufferedReader(new InputStreamReader(stream));) {
            String line;
            while ((line = reader.readLine()) != null) {
                Map<String, Object> map = mapper.readValue(line, new TypeReference<Map<String, Object>>() {
                });
                Integer entryId = (Integer) map.get("id");
                String title = (String) map.get("title");
                String content = (String) map.get("content");
                DateTime createdAt = new DateTime(((Map<String, Long>) map.get("created-at")).get("$date"));
                DateTime updatedAt = new DateTime(((Map<String, Long>) map.get("updated-at")).get("$date"));

                if (entryId.equals(165)) {
                    continue;
                }

                Entry entry = new Entry();
                entry.setEntryId(entryId);
                entry.setTitle(title);
                entry.setContents(content);
                entry.setPublished(true);
                entry.setFormat("md");
                entry.setCreatedDate(createdAt);
                entry.setCreatedBy("making");
                entry.setLastModifiedDate(updatedAt);
                entry.setLastModifiedBy("making");
                //entry.setCategory(Categories.fromCategory(((String) map.get("distinct-category")).replace("|", "::")).getCategories());

                List<Category> categories
                        = Categories.fromCategory(((String) map.get("distinct-category")).replace("|", "::")).getCategories();

                MapSqlParameterSource source = new MapSqlParameterSource()
                        .addValue("entryId", entry.getEntryId())
                        .addValue("createdBy", entry.getCreatedBy())
                        .addValue("createdAt", entry.getCreatedDate()/*.withZone(DateTimeZone.forTimeZone(TimeZone.getTimeZone("UTC")))*/.toDate())
                        .addValue("updatedBy", entry.getLastModifiedBy())
                        .addValue("updatedAt", entry.getLastModifiedDate()/*.withZone(DateTimeZone.forTimeZone(TimeZone.getTimeZone("UTC")))*/.toDate())
                        .addValue("contents", entry.getContents())
                        .addValue("format", entry.getFormat())
                        .addValue("published", entry.isPublished())
                        .addValue("title", entry.getTitle());

                jdbcTemplate.update("insert into entry (entry_id, version, created_by, created_date, last_modified_by, last_modified_date, contents, format, published, title) values (:entryId, 0, :createdBy, :createdAt, :updatedBy, :updatedAt, :contents, :format, :published, :title)", source);


                List<MapSqlParameterSource> sources = new ArrayList<>();
                for (Category c : categories) {
                    MapSqlParameterSource s = new MapSqlParameterSource()
                            .addValue("entryId", entry.getEntryId())
                            .addValue("categoryName", c.getCategoryName())
                            .addValue("categoryOrder", c.getCategoryPK().getCategoryOrder());

                    sources.add(s);
                }

                jdbcTemplate.batchUpdate("insert into category (category_name, category_order, entry_id) values (:categoryName, :categoryOrder, :entryId)", sources.toArray(new MapSqlParameterSource[categories.size()]));

                //break;
            }
        }
        System.out.println(entryRepository.findAll());
    }


}
