package am.ik.categolj2.api.entry;

import am.ik.categolj2.App;
import am.ik.categolj2.app.authentication.AuthenticationHelper;
import am.ik.categolj2.core.message.MessageKeys;
import am.ik.categolj2.domain.model.*;
import am.ik.categolj2.domain.repository.entry.EntryRepository;
import am.ik.categolj2.domain.repository.role.RoleRepository;
import am.ik.categolj2.domain.repository.user.UserRepository;
import am.ik.marked4j.MarkedBuilder;
import com.google.common.collect.Sets;
import com.jayway.restassured.RestAssured;
import com.jayway.restassured.config.RestAssuredConfig;
import com.jayway.restassured.config.SSLConfig;
import com.jayway.restassured.http.ContentType;
import com.jayway.restassured.response.Response;
import org.apache.http.conn.ssl.SSLContexts;
import org.apache.http.conn.ssl.SSLSocketFactory;
import org.apache.http.conn.ssl.TrustSelfSignedStrategy;
import org.joda.time.DateTime;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.IntegrationTest;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.common.OAuth2AccessToken;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import javax.net.ssl.SSLContext;
import java.util.Arrays;
import java.util.stream.Collectors;

import static com.jayway.restassured.RestAssured.given;
import static com.jayway.restassured.RestAssured.when;
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
public class EntryRestControllerIntegrationTest {
    static String datetimeFormat = "yyyy-MM-dd'T'HH:mm:ss.SSS'Z'";

    static {
        System.setProperty("user.timezone", "UTC");
    }

    @Value("${local.server.port}")
    int port;

    @Autowired
    EntryRepository entryRepository;
    @Autowired
    UserRepository userRepository;
    @Autowired
    RoleRepository roleRepository;
    @Autowired
    AuthenticationHelper authenticationHelper;
    @Autowired
    RestTemplate restTemplate;
    SSLSocketFactory sockectFactory;

    User admin;
    User editor;
    Entry entry1;
    Entry entry2;
    Entry entry3;
    Entry entry4;
    Entry entry5;

    @Autowired
    PasswordEncoder passwordEncoder;
    DateTime now = new DateTime();

    @Before
    public void setUp() throws Exception {
        SSLContext sslContext = SSLContexts.custom().loadTrustMaterial(null, new TrustSelfSignedStrategy()).useTLS().build();
        sockectFactory = new SSLSocketFactory(sslContext, SSLSocketFactory.ALLOW_ALL_HOSTNAME_VERIFIER);

        // clean data
        entryRepository.deleteAll();
        userRepository.deleteAll();
        roleRepository.deleteAll();
        entryRepository.flush();

        // initialize user
        Role adminRole = new Role(100, "ADMIN", null);
        Role editorRole = new Role(200, "EDITOR", null);
        roleRepository.save(Arrays.asList(adminRole, editorRole));
        roleRepository.flush();

        admin = new User("admin", passwordEncoder.encode("demo"), "admin@a.b", true, false, "Tarou", "Yamada", Sets.newHashSet(roleRepository.findOneByRoleName("ADMIN")));
        editor = new User("editor", passwordEncoder.encode("demo"), "editor@a.b", true, false, "Ichiro", "Suzuki", Sets.newHashSet(roleRepository.findOneByRoleName("EDITOR")));
        userRepository.save(Arrays.asList(admin, editor));
        userRepository.flush();

        // initialize entry
        entry1 = new Entry(null, "This is entry1!", "**Hello World1!**", "md", Arrays.asList(), true, Arrays.asList());
        entry1.setCreatedBy("admin");
        entry1.setCreatedDate(now);
        entry1.setLastModifiedBy("admin");
        entry1.setLastModifiedDate(now);
        entry1 = entryRepository.saveAndFlush(entry1);
        entry1.setCategory(Categories.fromCategory("aa::bb::cc").getCategories());
        entry1.getCategory().stream().forEach(c -> c.getCategoryPK().setEntryId(entry1.getEntryId()));

        entry2 = new Entry(null, "This is entry2!", "**Hello World2!**", "md", Arrays.asList(), false, Arrays.asList());
        entry2.setCreatedBy("admin");
        entry2.setCreatedDate(now.plus(2));
        entry2.setLastModifiedBy("admin");
        entry2.setLastModifiedDate(now.plus(2));
        entry2 = entryRepository.saveAndFlush(entry2);
        entry2.setCategory(Categories.fromCategory("aa::bb::cc").getCategories());
        entry2.getCategory().stream().forEach(c -> c.getCategoryPK().setEntryId(entry2.getEntryId()));

        entry3 = new Entry(null, "This is entry3!", "**Hello World3!**", "md", Arrays.asList(), true, Arrays.asList());
        entry3.setCreatedBy("editor");
        entry3.setCreatedDate(now.plus(3));
        entry3.setLastModifiedBy("editor");
        entry3.setLastModifiedDate(now.plus(3));
        entry3 = entryRepository.saveAndFlush(entry3);
        entry3.setCategory(Categories.fromCategory("aa::bb::cc").getCategories());
        entry3.getCategory().stream().forEach(c -> c.getCategoryPK().setEntryId(entry3.getEntryId()));

        entry4 = new Entry(null, "This is entry4!", "<h1>Hello World4!</h1>", "html", Arrays.asList(), true, Arrays.asList());
        entry4.setCreatedBy("editor");
        entry4.setCreatedDate(now.plus(4));
        entry4.setLastModifiedBy("editor");
        entry4.setLastModifiedDate(now.plus(4));
        entry4 = entryRepository.saveAndFlush(entry4);
        entry4.setCategory(Categories.fromCategory("aa::bb::cc").getCategories());
        entry4.getCategory().stream().forEach(c -> c.getCategoryPK().setEntryId(entry4.getEntryId()));

        entry5 = new Entry(null, "This is entry5!", "**Foo World5!**", "md", Arrays.asList(), true, Arrays.asList());
        entry5.setCreatedBy("editor");
        entry5.setCreatedDate(now.plus(5));
        entry5.setLastModifiedBy("editor");
        entry5.setLastModifiedDate(now.plus(5));
        entry5 = entryRepository.saveAndFlush(entry5);
        entry5.setCategory(Categories.fromCategory("aa::bb::dd::ee").getCategories());
        entry5.getCategory().stream().forEach(c -> c.getCategoryPK().setEntryId(entry5.getEntryId()));

        entryRepository.save(Arrays.asList(entry1, entry2, entry3, entry4, entry5));

        RestAssured.port = port;
        RestAssured.baseURI = "https://localhost";
        RestAssured.config = RestAssuredConfig.newConfig().sslConfig(new SSLConfig().sslSocketFactory(sockectFactory));
    }

    String getAccessToken(String username, String password) throws Exception {
        HttpEntity<MultiValueMap<String, Object>> ropRequest = authenticationHelper.createRopRequest(username, password);
        ResponseEntity<OAuth2AccessToken> result = restTemplate.postForEntity(RestAssured.baseURI + ":" + port + "/oauth/token", ropRequest, OAuth2AccessToken.class);
        return result.getBody().getValue();
    }

    @After
    public void tearDown() throws Exception {

    }

    @Test
    public void testGetEntries() throws Exception {
        when()
                .get("/api/v1/entries")
                .then()
                .log().all()
                .body("totalElements", is(4))
                .body("totalPages", is(1))
                .body("first", is(true))
                .body("last", is(true))
                .body("numberOfElements", is(4))
                .body("content[0].entryId", is(entry5.getEntryId()))
                .body("content[0].title", is(entry5.getTitle()))
                .body("content[0].contents", is(entry5.getContents()))
                .body("content[0].categoryName", is(entry5.getCategory().stream().map(Category::getCategoryName).collect(Collectors.toList())))
                .body("content[0].categoryString", is(entry5.getCategory().stream().map(Category::getCategoryName).collect(Collectors.joining(Categories.SEPARATOR))))
                .body("content[0].published", is(entry5.isPublished()))
                .body("content[0].createdBy", is(entry5.getCreatedBy()))
                .body("content[0].createdDate", is(entry5.getCreatedDate().toString(datetimeFormat)))
                .body("content[0].lastModifiedBy", is(entry5.getLastModifiedBy()))
                .body("content[0].lastModifiedDate", is(entry5.getLastModifiedDate().toString(datetimeFormat)))
                .body("content[1].entryId", is(entry4.getEntryId()))
                .body("content[1].title", is(entry4.getTitle()))
                .body("content[1].contents", is(entry4.getContents()))
                .body("content[1].categoryName", is(entry4.getCategory().stream().map(Category::getCategoryName).collect(Collectors.toList())))
                .body("content[1].categoryString", is(entry4.getCategory().stream().map(Category::getCategoryName).collect(Collectors.joining(Categories.SEPARATOR))))
                .body("content[1].published", is(entry4.isPublished()))
                .body("content[1].createdBy", is(entry4.getCreatedBy()))
                .body("content[1].createdDate", is(entry4.getCreatedDate().toString(datetimeFormat)))
                .body("content[1].lastModifiedBy", is(entry4.getLastModifiedBy()))
                .body("content[1].lastModifiedDate", is(entry4.getLastModifiedDate().toString(datetimeFormat)))
                .body("content[2].entryId", is(entry3.getEntryId()))
                .body("content[2].title", is(entry3.getTitle()))
                .body("content[2].contents", is(entry3.getContents()))
                .body("content[2].categoryName", is(entry3.getCategory().stream().map(Category::getCategoryName).collect(Collectors.toList())))
                .body("content[2].categoryString", is(entry3.getCategory().stream().map(Category::getCategoryName).collect(Collectors.joining(Categories.SEPARATOR))))
                .body("content[2].published", is(entry3.isPublished()))
                .body("content[2].createdBy", is(entry3.getCreatedBy()))
                .body("content[2].createdDate", is(entry3.getCreatedDate().toString(datetimeFormat)))
                .body("content[2].lastModifiedBy", is(entry3.getLastModifiedBy()))
                .body("content[2].lastModifiedDate", is(entry3.getLastModifiedDate().toString(datetimeFormat)))
                .body("content[3].entryId", is(entry1.getEntryId()))
                .body("content[3].title", is(entry1.getTitle()))
                .body("content[3].contents", is(entry1.getContents()))
                .body("content[3].categoryName", is(entry1.getCategory().stream().map(Category::getCategoryName).collect(Collectors.toList())))
                .body("content[3].categoryString", is(entry1.getCategory().stream().map(Category::getCategoryName).collect(Collectors.joining(Categories.SEPARATOR))))
                .body("content[3].published", is(entry1.isPublished()))
                .body("content[3].createdBy", is(entry1.getCreatedBy()))
                .body("content[3].createdDate", is(entry1.getCreatedDate().toString(datetimeFormat)))
                .body("content[3].lastModifiedBy", is(entry1.getLastModifiedBy()))
                .body("content[3].lastModifiedDate", is(entry1.getLastModifiedDate().toString(datetimeFormat)));
    }

    @Test
    public void testSearchEntries() throws Exception {
        String user = "admin";
        String accessToken = getAccessToken(user, "demo");
        Integer newEntryId = null;
        {
            Entry target = new Entry(null, "日本経済新聞", "日本経済新聞を読んでいません", "md", Categories.fromCategory("aa::bb::cc").getCategories(), true, Arrays.asList());
            target.setCreatedBy(user);
            target.setLastModifiedBy(user);
            EntryResource input = new EntryResource(null,
                    null,
                    target.getTitle(),
                    target.getContents(),
                    target.getFormat(),
                    Categories.toString(target.getCategory()),
                    null,
                    true,
                    true,
                    false,
                    null,
                    null,
                    null,
                    target.getCreatedBy(),
                    target.getLastModifiedBy());

            Response response = given()
                    .header("X-Admin", "true")
                    .header("Authorization", "Bearer " + accessToken)
                    .contentType(ContentType.JSON + ";charset=UTF-8")
                    .body(input)
                    .log().all()
                    .when()
                    .post("/api/v1/entries")
                    .then()
                    .log().all()
                    .statusCode(HttpStatus.CREATED.value())
                    .and().extract().response();
            newEntryId = response.path("entryId");
        }
        given()
                .param("keyword", "日本")
                .log().all()
                .when()
                .get("/api/v1/entries")
                .then()
                .log().all()
                .body("numberOfElements", is(1))
                .body("content[0].entryId", is(newEntryId));
        given()
                .param("keyword", "日本経済新聞")
                .log().all()
                .when()
                .get("/api/v1/entries")
                .then()
                .log().all()
                .body("numberOfElements", is(1))
                .body("content[0].entryId", is(newEntryId));
        given()
                .param("keyword", "経済")
                .log().all()
                .when()
                .get("/api/v1/entries")
                .then()
                .log().all()
                .body("numberOfElements", is(1))
                .body("content[0].entryId", is(newEntryId));
        given()
                .param("keyword", "新聞")
                .log().all()
                .when()
                .get("/api/v1/entries")
                .then()
                .log().all()
                .body("numberOfElements", is(1))
                .body("content[0].entryId", is(newEntryId));
        given()
                .param("keyword", "本経")
                .log().all()
                .when()
                .get("/api/v1/entries")
                .then()
                .log().all()
                .body("numberOfElements", is(0));
    }

    @Test
    public void testGetEntry_OK() throws Exception {
        Integer id = entry1.getId();
        Entry target = entry1;
        when()
                .get("/api/v1/entries/{id}", id)
                .then()
                .log().all()
                .statusCode(HttpStatus.OK.value())
                .body("entryId", is(id))
                .body("title", is(target.getTitle()))
                .body("format", is(target.getFormat()))
                .body("contents", is(target.getContents()))
                .body("categoryName", is(target.getCategory().stream().map(Category::getCategoryName).collect(Collectors.toList())))
                .body("categoryString", is(target.getCategory().stream().map(Category::getCategoryName).collect(Collectors.joining(Categories.SEPARATOR))))
                .body("published", is(target.isPublished()))
                .body("createdBy", is(target.getCreatedBy()))
                .body("createdDate", is(target.getCreatedDate().toString(datetimeFormat)))
                .body("lastModifiedBy", is(target.getLastModifiedBy()))
                .body("lastModifiedDate", is(target.getLastModifiedDate().toString(datetimeFormat)));
    }

    @Test
    public void testGetEntry_OK_FORMATTED() throws Exception {
        Integer id = entry1.getId();
        Entry target = entry1;
        given()
                .header("X-Formatted", "true")
                .when()
                .get("/api/v1/entries/{id}", id)
                .then()
                .log().all()
                .statusCode(HttpStatus.OK.value())
                .body("entryId", is(id))
                .body("title", is(target.getTitle()))
                .body("format", is(target.getFormat()))
                .body("contents", is(new MarkedBuilder().build().marked(target.getContents())))
                .body("categoryName", is(target.getCategory().stream().map(Category::getCategoryName).collect(Collectors.toList())))
                .body("categoryString", is(target.getCategory().stream().map(Category::getCategoryName).collect(Collectors.joining(Categories.SEPARATOR))))
                .body("published", is(target.isPublished()))
                .body("createdBy", is(target.getCreatedBy()))
                .body("createdDate", is(target.getCreatedDate().toString(datetimeFormat)))
                .body("lastModifiedBy", is(target.getLastModifiedBy()))
                .body("lastModifiedDate", is(target.getLastModifiedDate().toString(datetimeFormat)));
    }

    @Test
    public void testGetEntry_NOT_PUBLISHED() throws Exception {
        Integer id = entry2.getId();
        when()
                .get("/api/v1/entries/{id}", id)
                .then()
                .log().all()
                .statusCode(HttpStatus.NOT_FOUND.value())
                .body("code", is(MessageKeys.E_CT_EN_8201))
                .body("message", is("The requested entry is not found. [entryId=" + id + "]"));
    }

    @Test
    public void testGetEntry_NOT_EXIST() throws Exception {
        Integer id = Integer.MAX_VALUE;
        when()
                .get("/api/v1/entries/{id}", id)
                .then()
                .log().all()
                .statusCode(HttpStatus.NOT_FOUND.value())
                .body("code", is(MessageKeys.E_CT_EN_8201))
                .body("message", is(String.format("The requested entry is not found. [entryId=" + id + "]")));
    }

    @Test
    public void testGetEntriesByCategory() throws Exception {

    }

    @Test
    public void testGetEntriesByCreatedBy_ADMIN() throws Exception {
        String user = "admin";
        when()
                .get("/api/v1/users/{user}/entries", user)
                .then()
                .log().all()
                .body("totalElements", is(1))
                .body("totalPages", is(1))
                .body("first", is(true))
                .body("last", is(true))
                .body("numberOfElements", is(1))
                .body("content[0].entryId", is(entry1.getEntryId()))
                .body("content[0].title", is(entry1.getTitle()))
                .body("content[0].contents", is(entry1.getContents()))
                .body("content[0].categoryName", is(entry1.getCategory().stream().map(Category::getCategoryName).collect(Collectors.toList())))
                .body("content[0].categoryString", is(entry1.getCategory().stream().map(Category::getCategoryName).collect(Collectors.joining(Categories.SEPARATOR))))
                .body("content[0].published", is(entry1.isPublished()))
                .body("content[0].createdBy", is(entry1.getCreatedBy()))
                .body("content[0].createdDate", is(entry1.getCreatedDate().toString(datetimeFormat)))
                .body("content[0].lastModifiedBy", is(entry1.getLastModifiedBy()))
                .body("content[0].lastModifiedDate", is(entry1.getLastModifiedDate().toString(datetimeFormat)));
    }

    @Test
    public void testGetEntriesByCreatedBy_EDITOR() throws Exception {
        String user = "editor";
        when()
                .get("/api/v1/users/{user}/entries", user)
                .then()
                .log().all()
                .body("totalElements", is(3))
                .body("totalPages", is(1))
                .body("first", is(true))
                .body("last", is(true))
                .body("numberOfElements", is(3))
                .body("content[0].entryId", is(entry5.getEntryId()))
                .body("content[0].title", is(entry5.getTitle()))
                .body("content[0].contents", is(entry5.getContents()))
                .body("content[0].categoryName", is(entry5.getCategory().stream().map(Category::getCategoryName).collect(Collectors.toList())))
                .body("content[0].categoryString", is(entry5.getCategory().stream().map(Category::getCategoryName).collect(Collectors.joining(Categories.SEPARATOR))))
                .body("content[0].published", is(entry5.isPublished()))
                .body("content[0].createdBy", is(entry5.getCreatedBy()))
                .body("content[0].createdDate", is(entry5.getCreatedDate().toString(datetimeFormat)))
                .body("content[0].lastModifiedBy", is(entry5.getLastModifiedBy()))
                .body("content[0].lastModifiedDate", is(entry5.getLastModifiedDate().toString(datetimeFormat)))
                .body("content[1].entryId", is(entry4.getEntryId()))
                .body("content[1].title", is(entry4.getTitle()))
                .body("content[1].contents", is(entry4.getContents()))
                .body("content[1].categoryName", is(entry4.getCategory().stream().map(Category::getCategoryName).collect(Collectors.toList())))
                .body("content[1].categoryString", is(entry4.getCategory().stream().map(Category::getCategoryName).collect(Collectors.joining(Categories.SEPARATOR))))
                .body("content[1].published", is(entry4.isPublished()))
                .body("content[1].createdBy", is(entry4.getCreatedBy()))
                .body("content[1].createdDate", is(entry4.getCreatedDate().toString(datetimeFormat)))
                .body("content[1].lastModifiedBy", is(entry4.getLastModifiedBy()))
                .body("content[1].lastModifiedDate", is(entry4.getLastModifiedDate().toString(datetimeFormat)))
                .body("content[2].entryId", is(entry3.getEntryId()))
                .body("content[2].title", is(entry3.getTitle()))
                .body("content[2].contents", is(entry3.getContents()))
                .body("content[2].categoryName", is(entry3.getCategory().stream().map(Category::getCategoryName).collect(Collectors.toList())))
                .body("content[2].categoryString", is(entry3.getCategory().stream().map(Category::getCategoryName).collect(Collectors.joining(Categories.SEPARATOR))))
                .body("content[2].published", is(entry3.isPublished()))
                .body("content[2].createdBy", is(entry3.getCreatedBy()))
                .body("content[2].createdDate", is(entry3.getCreatedDate().toString(datetimeFormat)))
                .body("content[2].lastModifiedBy", is(entry3.getLastModifiedBy()))
                .body("content[2].lastModifiedDate", is(entry3.getLastModifiedDate().toString(datetimeFormat)));
    }

    @Test
    public void testGetEntriesInAdmin() throws Exception {
        String accessToken = getAccessToken("admin", "demo");
        given()
                .header("X-Admin", "true")
                .header("Authorization", "Bearer " + accessToken)
                .when()
                .get("/api/v1/entries")
                .then()
                .log().all()
                .body("totalElements", is(5))
                .body("totalPages", is(1))
                .body("first", is(true))
                .body("last", is(true))
                .body("numberOfElements", is(5))
                .body("content[0].entryId", is(entry5.getEntryId()))
                .body("content[0].title", is(entry5.getTitle()))
                .body("content[0].contents", is(entry5.getContents()))
                .body("content[0].categoryName", is(entry5.getCategory().stream().map(Category::getCategoryName).collect(Collectors.toList())))
                .body("content[0].categoryString", is(entry5.getCategory().stream().map(Category::getCategoryName).collect(Collectors.joining(Categories.SEPARATOR))))
                .body("content[0].published", is(entry5.isPublished()))
                .body("content[0].createdBy", is(entry5.getCreatedBy()))
                .body("content[0].createdDate", is(entry5.getCreatedDate().toString(datetimeFormat)))
                .body("content[0].lastModifiedBy", is(entry5.getLastModifiedBy()))
                .body("content[0].lastModifiedDate", is(entry5.getLastModifiedDate().toString(datetimeFormat)))
                .body("content[1].entryId", is(entry4.getEntryId()))
                .body("content[1].title", is(entry4.getTitle()))
                .body("content[1].contents", is(entry4.getContents()))
                .body("content[1].categoryName", is(entry4.getCategory().stream().map(Category::getCategoryName).collect(Collectors.toList())))
                .body("content[1].categoryString", is(entry4.getCategory().stream().map(Category::getCategoryName).collect(Collectors.joining(Categories.SEPARATOR))))
                .body("content[1].published", is(entry4.isPublished()))
                .body("content[1].createdBy", is(entry4.getCreatedBy()))
                .body("content[1].createdDate", is(entry4.getCreatedDate().toString(datetimeFormat)))
                .body("content[1].lastModifiedBy", is(entry4.getLastModifiedBy()))
                .body("content[1].lastModifiedDate", is(entry4.getLastModifiedDate().toString(datetimeFormat)))
                .body("content[2].entryId", is(entry3.getEntryId()))
                .body("content[2].title", is(entry3.getTitle()))
                .body("content[2].contents", is(entry3.getContents()))
                .body("content[2].categoryName", is(entry3.getCategory().stream().map(Category::getCategoryName).collect(Collectors.toList())))
                .body("content[2].categoryString", is(entry3.getCategory().stream().map(Category::getCategoryName).collect(Collectors.joining(Categories.SEPARATOR))))
                .body("content[2].published", is(entry3.isPublished()))
                .body("content[2].createdBy", is(entry3.getCreatedBy()))
                .body("content[2].createdDate", is(entry3.getCreatedDate().toString(datetimeFormat)))
                .body("content[2].lastModifiedBy", is(entry3.getLastModifiedBy()))
                .body("content[2].lastModifiedDate", is(entry3.getLastModifiedDate().toString(datetimeFormat)))
                .body("content[3].entryId", is(entry2.getEntryId()))
                .body("content[3].title", is(entry2.getTitle()))
                .body("content[3].contents", is(entry2.getContents()))
                .body("content[3].categoryName", is(entry2.getCategory().stream().map(Category::getCategoryName).collect(Collectors.toList())))
                .body("content[3].categoryString", is(entry2.getCategory().stream().map(Category::getCategoryName).collect(Collectors.joining(Categories.SEPARATOR))))
                .body("content[3].published", is(entry2.isPublished()))
                .body("content[3].createdBy", is(entry2.getCreatedBy()))
                .body("content[3].createdDate", is(entry2.getCreatedDate().toString(datetimeFormat)))
                .body("content[3].lastModifiedBy", is(entry2.getLastModifiedBy()))
                .body("content[3].lastModifiedDate", is(entry2.getLastModifiedDate().toString(datetimeFormat)))
                .body("content[4].entryId", is(entry1.getEntryId()))
                .body("content[4].title", is(entry1.getTitle()))
                .body("content[4].contents", is(entry1.getContents()))
                .body("content[4].categoryName", is(entry1.getCategory().stream().map(Category::getCategoryName).collect(Collectors.toList())))
                .body("content[4].categoryString", is(entry1.getCategory().stream().map(Category::getCategoryName).collect(Collectors.joining(Categories.SEPARATOR))))
                .body("content[4].published", is(entry1.isPublished()))
                .body("content[4].createdBy", is(entry1.getCreatedBy()))
                .body("content[4].createdDate", is(entry1.getCreatedDate().toString(datetimeFormat)))
                .body("content[4].lastModifiedBy", is(entry1.getLastModifiedBy()))
                .body("content[4].lastModifiedDate", is(entry1.getLastModifiedDate().toString(datetimeFormat)));
    }

    @Test
    public void testSearchEntriesInAdmin() throws Exception {

    }

    @Test
    public void testGetEntryInAdmin() throws Exception {
        Integer id = entry2.getId();
        Entry target = entry2;
        String accessToken = getAccessToken("admin", "demo");
        given()
                .header("X-Admin", "true")
                .header("Authorization", "Bearer " + accessToken)
                .log().all()
                .when()
                .get("/api/v1/entries/{id}", id)
                .then()
                .log().all()
                .statusCode(HttpStatus.OK.value())
                .body("entryId", is(id))
                .body("title", is(target.getTitle()))
                .body("format", is(target.getFormat()))
                .body("contents", is(target.getContents()))
                .body("categoryName", is(target.getCategory().stream().map(Category::getCategoryName).collect(Collectors.toList())))
                .body("categoryString", is(target.getCategory().stream().map(Category::getCategoryName).collect(Collectors.joining(Categories.SEPARATOR))))
                .body("published", is(target.isPublished()))
                .body("createdBy", is(target.getCreatedBy()))
                .body("createdDate", is(target.getCreatedDate().toString(datetimeFormat)))
                .body("lastModifiedBy", is(target.getLastModifiedBy()))
                .body("lastModifiedDate", is(target.getLastModifiedDate().toString(datetimeFormat)));
    }

    @Test
    public void testCreateEntryInAdmin() throws Exception {
        String user = "admin";
        String accessToken = getAccessToken(user, "demo");
        Entry target = new Entry(null, "New Entry!!", "**New!!**", "md", Categories.fromCategory("aa::bb::cc").getCategories(), true, Arrays.asList());
        target.setCreatedBy(user);
        target.setLastModifiedBy(user);
        EntryResource input = new EntryResource(null,
                null,
                target.getTitle(),
                target.getContents(),
                target.getFormat(),
                Categories.toString(target.getCategory()),
                null,
                true,
                true,
                false,
                null,
                null,
                null,
                target.getCreatedBy(),
                target.getLastModifiedBy());

        Response response = given()
                .header("X-Admin", "true")
                .header("Authorization", "Bearer " + accessToken)
                .contentType(ContentType.JSON)
                .body(input)
                .when()
                .post("/api/v1/entries")
                .then()
                .log().all()
                .statusCode(HttpStatus.CREATED.value())
                .body("title", is(target.getTitle()))
                .body("format", is(target.getFormat()))
                .body("contents", is(target.getContents()))
                .body("categoryName", is(target.getCategory().stream().map(Category::getCategoryName).collect(Collectors.toList())))
                .body("categoryString", is(target.getCategory().stream().map(Category::getCategoryName).collect(Collectors.joining(Categories.SEPARATOR))))
                .body("published", is(target.isPublished()))
                .body("createdBy", is(target.getCreatedBy()))
                .body("lastModifiedBy", is(target.getLastModifiedBy()))
                .and().extract().response();
        Integer newEntryId = response.path("entryId");
        when()
                .get("/api/v1/entries/{id}", newEntryId)
                .then()
                .log().all()
                .statusCode(HttpStatus.OK.value())
                .body("entryId", is(newEntryId))
                .body("title", is(target.getTitle()))
                .body("format", is(target.getFormat()))
                .body("contents", is(target.getContents()))
                .body("categoryName", is(target.getCategory().stream().map(Category::getCategoryName).collect(Collectors.toList())))
                .body("categoryString", is(target.getCategory().stream().map(Category::getCategoryName).collect(Collectors.joining(Categories.SEPARATOR))))
                .body("published", is(target.isPublished()))
                .body("createdBy", is(target.getCreatedBy()))
                .body("lastModifiedBy", is(target.getLastModifiedBy()));
    }


    @Test
    public void testUpdateEntryInAdmin_CHANGE_TITLE() throws Exception {
        String user = "admin";
        String accessToken = getAccessToken(user, "demo");
        Entry target = entry1;
        target.setTitle("Title is changed!");
        target.setLastModifiedBy(user);

        EntryResource input = new EntryResource(null,
                null,
                target.getTitle(),
                target.getContents(),
                target.getFormat(),
                Categories.toString(target.getCategory()),
                null,
                true,
                true,
                false,
                null,
                null,
                null,
                target.getCreatedBy(),
                target.getLastModifiedBy());

        Response response = given()
                .header("X-Admin", "true")
                .header("Authorization", "Bearer " + accessToken)
                .contentType(ContentType.JSON)
                .body(input)
                .when()
                .put("/api/v1/entries/{id}", target.getEntryId())
                .then()
                .log().all()
                .statusCode(HttpStatus.OK.value())
                .body("entryId", is(target.getEntryId()))
                .body("title", is(target.getTitle()))
                .body("format", is(target.getFormat()))
                .body("contents", is(target.getContents()))
                .body("categoryName", is(target.getCategory().stream().map(Category::getCategoryName).collect(Collectors.toList())))
                .body("categoryString", is(target.getCategory().stream().map(Category::getCategoryName).collect(Collectors.joining(Categories.SEPARATOR))))
                .body("published", is(target.isPublished()))
                .body("createdBy", is(target.getCreatedBy()))
                .body("lastModifiedBy", is(target.getLastModifiedBy()))
                .and().extract().response();

        when()
                .get("/api/v1/entries/{id}", target.getEntryId())
                .then()
                .log().all()
                .statusCode(HttpStatus.OK.value())
                .body("entryId", is(target.getEntryId()))
                .body("title", is(target.getTitle()))
                .body("format", is(target.getFormat()))
                .body("contents", is(target.getContents()))
                .body("categoryName", is(target.getCategory().stream().map(Category::getCategoryName).collect(Collectors.toList())))
                .body("categoryString", is(target.getCategory().stream().map(Category::getCategoryName).collect(Collectors.joining(Categories.SEPARATOR))))
                .body("published", is(target.isPublished()))
                .body("createdBy", is(target.getCreatedBy()))
                .body("lastModifiedBy", is(target.getLastModifiedBy()));
    }

    @Test
    public void testUpdateEntryInAdmin_CHANGE_CONTENTS() throws Exception {
        String user = "admin";
        String accessToken = getAccessToken(user, "demo");
        Entry target = entry1;
        target.setContents("**Contents are changed!**");
        target.setLastModifiedBy(user);

        EntryResource input = new EntryResource(null,
                null,
                target.getTitle(),
                target.getContents(),
                target.getFormat(),
                Categories.toString(target.getCategory()),
                null,
                true,
                true,
                false,
                null,
                null,
                null,
                target.getCreatedBy(),
                target.getLastModifiedBy());

        Response response = given()
                .header("X-Admin", "true")
                .header("Authorization", "Bearer " + accessToken)
                .contentType(ContentType.JSON)
                .body(input)
                .when()
                .put("/api/v1/entries/{id}", target.getEntryId())
                .then()
                .log().all()
                .statusCode(HttpStatus.OK.value())
                .body("entryId", is(target.getEntryId()))
                .body("title", is(target.getTitle()))
                .body("format", is(target.getFormat()))
                .body("contents", is(target.getContents()))
                .body("categoryName", is(target.getCategory().stream().map(Category::getCategoryName).collect(Collectors.toList())))
                .body("categoryString", is(target.getCategory().stream().map(Category::getCategoryName).collect(Collectors.joining(Categories.SEPARATOR))))
                .body("published", is(target.isPublished()))
                .body("createdBy", is(target.getCreatedBy()))
                .body("lastModifiedBy", is(target.getLastModifiedBy()))
                .and().extract().response();

        when()
                .get("/api/v1/entries/{id}", target.getEntryId())
                .then()
                .log().all()
                .statusCode(HttpStatus.OK.value())
                .body("entryId", is(target.getEntryId()))
                .body("title", is(target.getTitle()))
                .body("format", is(target.getFormat()))
                .body("contents", is(target.getContents()))
                .body("categoryName", is(target.getCategory().stream().map(Category::getCategoryName).collect(Collectors.toList())))
                .body("categoryString", is(target.getCategory().stream().map(Category::getCategoryName).collect(Collectors.joining(Categories.SEPARATOR))))
                .body("published", is(target.isPublished()))
                .body("createdBy", is(target.getCreatedBy()))
                .body("lastModifiedBy", is(target.getLastModifiedBy()));
    }

    @Test
    public void testUpdateEntryInAdmin_CHANGE_FORMAT_AND_CONTENTS() throws Exception {
        String user = "admin";
        String accessToken = getAccessToken(user, "demo");
        Entry target = entry1;
        target.setFormat("html");
        target.setContents("<strong>Contents are changed!</strong>");
        target.setLastModifiedBy(user);

        EntryResource input = new EntryResource(null,
                null,
                target.getTitle(),
                target.getContents(),
                target.getFormat(),
                Categories.toString(target.getCategory()),
                null,
                true,
                true,
                false,
                null,
                null,
                null,
                target.getCreatedBy(),
                target.getLastModifiedBy());

        Response response = given()
                .header("X-Admin", "true")
                .header("Authorization", "Bearer " + accessToken)
                .contentType(ContentType.JSON)
                .body(input)
                .when()
                .put("/api/v1/entries/{id}", target.getEntryId())
                .then()
                .log().all()
                .statusCode(HttpStatus.OK.value())
                .body("entryId", is(target.getEntryId()))
                .body("title", is(target.getTitle()))
                .body("format", is(target.getFormat()))
                .body("contents", is(target.getContents()))
                .body("categoryName", is(target.getCategory().stream().map(Category::getCategoryName).collect(Collectors.toList())))
                .body("categoryString", is(target.getCategory().stream().map(Category::getCategoryName).collect(Collectors.joining(Categories.SEPARATOR))))
                .body("published", is(target.isPublished()))
                .body("createdBy", is(target.getCreatedBy()))
                .body("lastModifiedBy", is(target.getLastModifiedBy()))
                .and().extract().response();

        when()
                .get("/api/v1/entries/{id}", target.getEntryId())
                .then()
                .log().all()
                .statusCode(HttpStatus.OK.value())
                .body("entryId", is(target.getEntryId()))
                .body("title", is(target.getTitle()))
                .body("format", is(target.getFormat()))
                .body("contents", is(target.getContents()))
                .body("categoryName", is(target.getCategory().stream().map(Category::getCategoryName).collect(Collectors.toList())))
                .body("categoryString", is(target.getCategory().stream().map(Category::getCategoryName).collect(Collectors.joining(Categories.SEPARATOR))))
                .body("published", is(target.isPublished()))
                .body("createdBy", is(target.getCreatedBy()))
                .body("lastModifiedBy", is(target.getLastModifiedBy()));
    }


    @Test
    public void testUpdateEntryInAdmin_ADD_CATEGORY() throws Exception {
        String user = "admin";
        String accessToken = getAccessToken(user, "demo");
        Entry target = entry1;
        target.setCategory(Categories.fromCategory("aa::bb::cc::dd").getCategories());
        target.setLastModifiedBy(user);

        EntryResource input = new EntryResource(null,
                null,
                target.getTitle(),
                target.getContents(),
                target.getFormat(),
                Categories.toString(target.getCategory()),
                null,
                true,
                true,
                false,
                null,
                null,
                null,
                target.getCreatedBy(),
                target.getLastModifiedBy());

        Response response = given()
                .header("X-Admin", "true")
                .header("Authorization", "Bearer " + accessToken)
                .contentType(ContentType.JSON)
                .body(input)
                .when()
                .put("/api/v1/entries/{id}", target.getEntryId())
                .then()
                .log().all()
                .statusCode(HttpStatus.OK.value())
                .body("entryId", is(target.getEntryId()))
                .body("title", is(target.getTitle()))
                .body("format", is(target.getFormat()))
                .body("contents", is(target.getContents()))
                .body("categoryName", is(target.getCategory().stream().map(Category::getCategoryName).collect(Collectors.toList())))
                .body("categoryString", is(target.getCategory().stream().map(Category::getCategoryName).collect(Collectors.joining(Categories.SEPARATOR))))
                .body("published", is(target.isPublished()))
                .body("createdBy", is(target.getCreatedBy()))
                .body("lastModifiedBy", is(target.getLastModifiedBy()))
                .and().extract().response();

        when()
                .get("/api/v1/entries/{id}", target.getEntryId())
                .then()
                .log().all()
                .statusCode(HttpStatus.OK.value())
                .body("entryId", is(target.getEntryId()))
                .body("title", is(target.getTitle()))
                .body("format", is(target.getFormat()))
                .body("contents", is(target.getContents()))
                .body("categoryName", is(target.getCategory().stream().map(Category::getCategoryName).collect(Collectors.toList())))
                .body("categoryString", is(target.getCategory().stream().map(Category::getCategoryName).collect(Collectors.joining(Categories.SEPARATOR))))
                .body("published", is(target.isPublished()))
                .body("createdBy", is(target.getCreatedBy()))
                .body("lastModifiedBy", is(target.getLastModifiedBy()));
    }

    @Test
    public void testUpdateEntryInAdmin_REDUCE_CATEGORY() throws Exception {
        String user = "admin";
        String accessToken = getAccessToken(user, "demo");
        Entry target = entry1;
        target.setCategory(Categories.fromCategory("aa::bb").getCategories());
        target.setLastModifiedBy(user);

        EntryResource input = new EntryResource(null,
                null,
                target.getTitle(),
                target.getContents(),
                target.getFormat(),
                Categories.toString(target.getCategory()),
                null,
                true,
                true,
                false,
                null,
                null,
                null,
                target.getCreatedBy(),
                target.getLastModifiedBy());

        Response response = given()
                .header("X-Admin", "true")
                .header("Authorization", "Bearer " + accessToken)
                .contentType(ContentType.JSON)
                .body(input)
                .when()
                .put("/api/v1/entries/{id}", target.getEntryId())
                .then()
                .log().all()
                .statusCode(HttpStatus.OK.value())
                .body("entryId", is(target.getEntryId()))
                .body("title", is(target.getTitle()))
                .body("format", is(target.getFormat()))
                .body("contents", is(target.getContents()))
                .body("categoryName", is(target.getCategory().stream().map(Category::getCategoryName).collect(Collectors.toList())))
                .body("categoryString", is(target.getCategory().stream().map(Category::getCategoryName).collect(Collectors.joining(Categories.SEPARATOR))))
                .body("published", is(target.isPublished()))
                .body("createdBy", is(target.getCreatedBy()))
                .body("lastModifiedBy", is(target.getLastModifiedBy()))
                .and().extract().response();

        when()
                .get("/api/v1/entries/{id}", target.getEntryId())
                .then()
                .log().all()
                .statusCode(HttpStatus.OK.value())
                .body("entryId", is(target.getEntryId()))
                .body("title", is(target.getTitle()))
                .body("format", is(target.getFormat()))
                .body("contents", is(target.getContents()))
                .body("categoryName", is(target.getCategory().stream().map(Category::getCategoryName).collect(Collectors.toList())))
                .body("categoryString", is(target.getCategory().stream().map(Category::getCategoryName).collect(Collectors.joining(Categories.SEPARATOR))))
                .body("published", is(target.isPublished()))
                .body("createdBy", is(target.getCreatedBy()))
                .body("lastModifiedBy", is(target.getLastModifiedBy()));
    }

    @Test
    public void testUpdateEntryInAdmin_REPLACE_CATEGORY() throws Exception {
        String user = "admin";
        String accessToken = getAccessToken(user, "demo");
        Entry target = entry1;
        target.setCategory(Categories.fromCategory("xx::yy::zz").getCategories());
        target.setLastModifiedBy(user);

        EntryResource input = new EntryResource(null,
                null,
                target.getTitle(),
                target.getContents(),
                target.getFormat(),
                Categories.toString(target.getCategory()),
                null,
                true,
                true,
                false,
                null,
                null,
                null,
                target.getCreatedBy(),
                target.getLastModifiedBy());

        Response response = given()
                .header("X-Admin", "true")
                .header("Authorization", "Bearer " + accessToken)
                .contentType(ContentType.JSON)
                .body(input)
                .when()
                .put("/api/v1/entries/{id}", target.getEntryId())
                .then()
                .log().all()
                .statusCode(HttpStatus.OK.value())
                .body("entryId", is(target.getEntryId()))
                .body("title", is(target.getTitle()))
                .body("format", is(target.getFormat()))
                .body("contents", is(target.getContents()))
                .body("categoryName", is(target.getCategory().stream().map(Category::getCategoryName).collect(Collectors.toList())))
                .body("categoryString", is(target.getCategory().stream().map(Category::getCategoryName).collect(Collectors.joining(Categories.SEPARATOR))))
                .body("published", is(target.isPublished()))
                .body("createdBy", is(target.getCreatedBy()))
                .body("lastModifiedBy", is(target.getLastModifiedBy()))
                .and().extract().response();

        when()
                .get("/api/v1/entries/{id}", target.getEntryId())
                .then()
                .log().all()
                .statusCode(HttpStatus.OK.value())
                .body("entryId", is(target.getEntryId()))
                .body("title", is(target.getTitle()))
                .body("format", is(target.getFormat()))
                .body("contents", is(target.getContents()))
                .body("categoryName", is(target.getCategory().stream().map(Category::getCategoryName).collect(Collectors.toList())))
                .body("categoryString", is(target.getCategory().stream().map(Category::getCategoryName).collect(Collectors.joining(Categories.SEPARATOR))))
                .body("published", is(target.isPublished()))
                .body("createdBy", is(target.getCreatedBy()))
                .body("lastModifiedBy", is(target.getLastModifiedBy()));
    }

    @Test
    public void testDeleteEntryInAdmin() throws Exception {
        Integer id = entry2.getId();
        String accessToken = getAccessToken("admin", "demo");
        given()
                .header("X-Admin", "true")
                .header("Authorization", "Bearer " + accessToken)
                .log().all()
                .when()
                .delete("/api/v1/entries/{id}", id)
                .then()
                .log().all()
                .statusCode(HttpStatus.NO_CONTENT.value());
        when()
                .get("/api/v1/entries/{id}", id)
                .then()
                .log().all()
                .statusCode(HttpStatus.NOT_FOUND.value())
                .body("code", is(MessageKeys.E_CT_EN_8201))
                .body("message", is(String.format("The requested entry is not found. [entryId=" + id + "]")));
    }
}