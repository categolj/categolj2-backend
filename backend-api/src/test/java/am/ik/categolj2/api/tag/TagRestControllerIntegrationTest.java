package am.ik.categolj2.api.tag;

import am.ik.categolj2.App;
import am.ik.categolj2.domain.model.Categories;
import am.ik.categolj2.domain.model.Entry;
import am.ik.categolj2.domain.model.Tag;
import am.ik.categolj2.domain.repository.entry.EntryRepository;
import am.ik.categolj2.domain.repository.tag.TagRepository;
import com.google.common.collect.Sets;
import com.jayway.restassured.RestAssured;
import com.jayway.restassured.config.RestAssuredConfig;
import com.jayway.restassured.config.SSLConfig;
import org.apache.http.conn.ssl.SSLContexts;
import org.apache.http.conn.ssl.SSLSocketFactory;
import org.apache.http.conn.ssl.TrustSelfSignedStrategy;
import org.joda.time.DateTime;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.IntegrationTest;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.http.HttpStatus;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;

import javax.net.ssl.SSLContext;
import java.util.Arrays;
import java.util.Collections;

import static com.jayway.restassured.RestAssured.given;
import static org.hamcrest.Matchers.emptyIterable;
import static org.hamcrest.Matchers.is;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = App.class)
@WebAppConfiguration
@IntegrationTest({"server.port:0",
        "server-http.port:0",
        "spring.datasource.url:jdbc:h2:mem:bookmark;DB_CLOSE_ON_EXIT=FALSE",
        "flyway.enabled:false",
        "spring.jpa.hibernate.generate-ddl:true",
        "spring.jpa.hibernate.ddl-auto:create-drop",
        "endpoints.autoconfig.enabled:false",
        "endpoints.beans.enabled:false",
        "endpoints.configprops.enabled:false",
        "endpoints.dump.enabled:false",
        "endpoints.env.enabled:false",
        "endpoints.health.enabled:false",
        "endpoints.metrics.enabled:false",
        "endpoints.shutdown.enabled:false",
        "endpoints.trace.enabled:false",
        "endpoints.jmx.enabled:false"})
public class TagRestControllerIntegrationTest {
    static {
        System.setProperty("user.timezone", "UTC");
    }

    @Value("${local.server.port}")
    int port;
    @Autowired
    EntryRepository entryRepository;
    @Autowired
    TagRepository tagRepository;
    SSLSocketFactory sockectFactory;
    DateTime now = new DateTime();
    Entry entry1, entry2;


    @Before
    public void setUp() throws Exception {
        SSLContext sslContext = SSLContexts.custom().loadTrustMaterial(null, new TrustSelfSignedStrategy()).useTLS().build();
        sockectFactory = new SSLSocketFactory(sslContext, SSLSocketFactory.ALLOW_ALL_HOSTNAME_VERIFIER);

        // clean data
        entryRepository.deleteAll();
        tagRepository.deleteAll();

        // initialize entry
        entry1 = new Entry(null, "This is entry1!", "**Hello World1!**", "md", Arrays.asList(), true, Arrays.asList(), Collections.<Tag>emptySet());
        entry1.setCreatedBy("admin");
        entry1.setCreatedDate(now);
        entry1.setLastModifiedBy("admin");
        entry1.setLastModifiedDate(now);
        entry1 = entryRepository.saveAndFlush(entry1);
        entry1.setCategory(Categories.fromCategory("aa::bb::cc").getCategories());
        entry1.getCategory().stream().forEach(c -> c.getCategoryPK().setEntryId(entry1.getEntryId()));
        entry1.setTags(Sets.newHashSet(new Tag("Java"), new Tag("Spring")));

        entry2 = new Entry(null, "This is entry2!", "**Hello World2!**", "md", Arrays.asList(), false, Arrays.asList(), Collections.<Tag>emptySet());
        entry2.setCreatedBy("admin");
        entry2.setCreatedDate(now.plus(2));
        entry2.setLastModifiedBy("admin");
        entry2.setLastModifiedDate(now.plus(2));
        entry2 = entryRepository.saveAndFlush(entry2);
        entry2.setCategory(Categories.fromCategory("aa::bb::cc").getCategories());
        entry2.getCategory().stream().forEach(c -> c.getCategoryPK().setEntryId(entry2.getEntryId()));
        entry2.setTags(Sets.newHashSet(new Tag("Java"), new Tag("Java EE")));

        entryRepository.save(Arrays.asList(entry1, entry2));
        entryRepository.flush();

        RestAssured.port = port;
        RestAssured.baseURI = "https://localhost";
        RestAssured.config = RestAssuredConfig.newConfig().sslConfig(new SSLConfig().sslSocketFactory(sockectFactory));
    }


    @Test
    public void testGetTags() throws Exception {
        given()
                .log().all()
                .when()
                .get("/api/v1/tags")
                .then()
                .log().all()
                .statusCode(HttpStatus.OK.value())
                .body("[0].tagName", is("Java"))
                .body("[1].tagName", is("Java EE"))
                .body("[2].tagName", is("Spring"));
    }

    @Test
    public void testSearchTags() throws Exception {
        given()
                .param("tagName", "Ja")
                .log().all()
                .when()
                .get("/api/v1/tags")
                .then()
                .log().all()
                .statusCode(HttpStatus.OK.value())
                .body("[0].tagName", is("Java"))
                .body("[1].tagName", is("Java EE"));

        given()
                .param("tagName", "Sp")
                .log().all()
                .when()
                .get("/api/v1/tags")
                .then()
                .log().all()
                .statusCode(HttpStatus.OK.value())
                .body("[0].tagName", is("Spring"));
    }

    @Test
    public void testSearchTags_SINGLE_STRING() throws Exception {
        given()
                .param("tagName", "J")
                .log().all()
                .when()
                .get("/api/v1/tags")
                .then()
                .log().all()
                .statusCode(HttpStatus.OK.value())
                .body("$", emptyIterable());
    }
}