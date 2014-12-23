package am.ik.categolj2.api.link;

import am.ik.categolj2.App;
import am.ik.categolj2.app.authentication.AuthenticationHelper;
import am.ik.categolj2.domain.model.Link;
import am.ik.categolj2.domain.model.Role;
import am.ik.categolj2.domain.model.User;
import am.ik.categolj2.domain.repository.link.LinkRepository;
import am.ik.categolj2.domain.repository.role.RoleRepository;
import am.ik.categolj2.domain.repository.user.UserRepository;
import com.google.common.collect.Sets;
import com.jayway.restassured.RestAssured;
import com.jayway.restassured.config.RestAssuredConfig;
import com.jayway.restassured.config.SSLConfig;
import com.jayway.restassured.http.ContentType;
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
import org.springframework.cache.CacheManager;
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

import static com.jayway.restassured.RestAssured.given;
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
public class LinkRestControllerIntegrationTest {
    static {
        System.setProperty("user.timezone", "UTC");
    }
    @Value("${local.server.port}")
    int port;
    @Autowired
    LinkRepository linkRepository;
    @Autowired
    UserRepository userRepository;
    @Autowired
    RoleRepository roleRepository;
    @Autowired
    AuthenticationHelper authenticationHelper;
    @Autowired
    RestTemplate restTemplate;
    @Autowired
    CacheManager cacheManager;
    SSLSocketFactory sockectFactory;
    User admin;
    User editor;
    Link link1;
    Link link2;

    @Autowired
    PasswordEncoder passwordEncoder;
    DateTime now = new DateTime();

    @Before
    public void setUp() throws Exception {
        SSLContext sslContext = SSLContexts.custom().loadTrustMaterial(null, new TrustSelfSignedStrategy()).useTLS().build();
        sockectFactory = new SSLSocketFactory(sslContext, SSLSocketFactory.ALLOW_ALL_HOSTNAME_VERIFIER);

        // clean data
        linkRepository.deleteAll();
        userRepository.deleteAll();
        roleRepository.deleteAll();
        linkRepository.flush();
        cacheManager.getCache("link").clear();

        // initialize user
        Role adminRole = new Role(100, "ADMIN", null);
        Role editorRole = new Role(200, "EDITOR", null);
        roleRepository.save(Arrays.asList(adminRole, editorRole));
        roleRepository.flush();

        admin = new User("admin", passwordEncoder.encode("demo"), "admin@a.b", true, false, "Tarou", "Yamada", Sets.newHashSet(roleRepository.findOneByRoleName("ADMIN")));
        editor = new User("editor", passwordEncoder.encode("demo"), "editor@a.b", true, false, "Ichiro", "Suzuki", Sets.newHashSet(roleRepository.findOneByRoleName("EDITOR")));
        userRepository.save(Arrays.asList(admin, editor));
        userRepository.flush();

        // initialize link
        link1 = new Link("https://google.com", "Google");
        link1.setCreatedBy(admin.getUsername());
        link1.setCreatedDate(now.minusDays(1).minusMinutes(2));
        link1.setLastModifiedBy(admin.getUsername());
        link1.setLastModifiedDate(now.minusDays(1).minusMinutes(2));
        link2 = new Link("http://bit.ly/hajiboot", "Hajimete no Spring Boot");
        link2.setCreatedBy(admin.getUsername());
        link2.setCreatedDate(now.minusDays(1).minusMinutes(1));
        link2.setLastModifiedBy(admin.getUsername());
        link2.setLastModifiedDate(now.minusDays(1).minusMillis(1));
        linkRepository.save(Arrays.asList(link1, link2));
        linkRepository.flush();

        RestAssured.port = port;
        RestAssured.baseURI = "https://localhost";
        RestAssured.config = RestAssuredConfig.newConfig().sslConfig(new SSLConfig().sslSocketFactory(sockectFactory));
    }

    String getAccessToken(String username, String password) throws Exception {
        HttpEntity<MultiValueMap<String, Object>> ropRequest = authenticationHelper.createRopRequest(username, password);
        ResponseEntity<OAuth2AccessToken> result = restTemplate.postForEntity(RestAssured.baseURI + ":" + port + "/oauth/token", ropRequest, OAuth2AccessToken.class);
        return result.getBody().getValue();
    }

    @Test
    public void testGetLinks() throws Exception {
        given()
                .log().all()
                .when()
                .get("/api/v1/links")
                .then()
                .log().all()
                .statusCode(HttpStatus.OK.value())
                .body("[0].id", is(link2.getId()))
                .body("[0].url", is(link2.getUrl()))
                .body("[0].linkName", is(link2.getLinkName()))
                .body("[1].id", is(link1.getId()))
                .body("[1].url", is(link1.getUrl()))
                .body("[1].linkName", is(link1.getLinkName()));
    }

    @Test
    public void testPostLinks() throws Exception {
        LinkResource input = new LinkResource("http://blog.ik.am", "http://blog.ik.am", "BLOG");
        String user = "admin";
        String accessToken = getAccessToken(user, "demo");
        given()
                .header("X-Admin", "true")
                .header("Authorization", "Bearer " + accessToken)
                .contentType(ContentType.JSON)
                .body(input)
                .when()
                .post("/api/v1/links")
                .then()
                .log().all()
                .statusCode(HttpStatus.CREATED.value())
                .body("id", is(input.getId()))
                .body("url", is(input.getUrl()))
                .body("linkName", is(input.getLinkName()));
        given()
                .log().all()
                .when()
                .get("/api/v1/links")
                .then()
                .log().all()
                .statusCode(HttpStatus.OK.value())
                .body("[0].id", is(input.getId()))
                .body("[0].url", is(input.getUrl()))
                .body("[0].linkName", is(input.getLinkName()))
                .body("[1].id", is(link2.getId()))
                .body("[1].url", is(link2.getUrl()))
                .body("[1].linkName", is(link2.getLinkName()))
                .body("[2].id", is(link1.getId()))
                .body("[2].url", is(link1.getUrl()))
                .body("[2].linkName", is(link1.getLinkName()));
    }

    @Test
    public void testPutLinks() throws Exception {
        LinkResource input = new LinkResource("https://google.com", "https://google.com", "Google.com");
        String user = "admin";
        String accessToken = getAccessToken(user, "demo");
        given()
                .header("X-Admin", "true")
                .header("Authorization", "Bearer " + accessToken)
                .contentType(ContentType.JSON)
                .body(input)
                .when()
                .put("/api/v1/links/{url}", input.getUrl())
                .then()
                .log().all()
                .statusCode(HttpStatus.OK.value())
                .body("id", is(input.getId()))
                .body("url", is(input.getUrl()))
                .body("linkName", is(input.getLinkName()));
        given()
                .log().all()
                .when()
                .get("/api/v1/links")
                .then()
                .log().all()
                .statusCode(HttpStatus.OK.value())
                .body("[0].id", is(input.getId()))
                .body("[0].url", is(input.getUrl()))
                .body("[0].linkName", is(input.getLinkName()))
                .body("[1].id", is(link2.getId()))
                .body("[1].url", is(link2.getUrl()))
                .body("[1].linkName", is(link2.getLinkName()));
    }

    @Test
    public void testDeleteLinks() throws Exception {
        LinkResource input = new LinkResource("https://google.com", "https://google.com", "Google.com");
        String user = "admin";
        String accessToken = getAccessToken(user, "demo");
        given()
                .header("X-Admin", "true")
                .header("Authorization", "Bearer " + accessToken)
                .contentType(ContentType.JSON)
                .body(input)
                .when()
                .delete("/api/v1/links/{url}", input.getUrl())
                .then()
                .log().all()
                .statusCode(HttpStatus.NO_CONTENT.value());
        given()
                .log().all()
                .when()
                .get("/api/v1/links")
                .then()
                .log().all()
                .statusCode(HttpStatus.OK.value())
                .body("[0].id", is(link2.getId()))
                .body("[0].url", is(link2.getUrl()))
                .body("[0].linkName", is(link2.getLinkName()));
    }
}