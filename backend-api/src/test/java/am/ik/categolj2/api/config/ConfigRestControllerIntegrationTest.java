package am.ik.categolj2.api.config;

import am.ik.categolj2.App;
import am.ik.categolj2.app.authentication.AuthenticationHelper;
import am.ik.categolj2.core.message.MessageKeys;
import am.ik.categolj2.domain.model.Config;
import am.ik.categolj2.domain.model.Role;
import am.ik.categolj2.domain.model.User;
import am.ik.categolj2.domain.repository.config.ConfigRepository;
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

import static com.jayway.restassured.RestAssured.*;
import static org.hamcrest.Matchers.*;

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
public class ConfigRestControllerIntegrationTest {
    static {
        System.setProperty("user.timezone", "UTC");
    }

    @Value("${local.server.port}")
    int port;
    @Autowired
    ConfigRepository configRepository;
    @Autowired
    UserRepository userRepository;
    @Autowired
    RoleRepository roleRepository;
    @Autowired
    AuthenticationHelper authenticationHelper;
    @Autowired
    RestTemplate restTemplate;
    @Autowired
    PasswordEncoder passwordEncoder;

    SSLSocketFactory sockectFactory;
    DateTime now = new DateTime();
    Config conf1;
    Config conf2;
    User admin;

    @Before
    public void setUp() throws Exception {
        SSLContext sslContext = SSLContexts.custom().loadTrustMaterial(null, new TrustSelfSignedStrategy()).useTLS().build();
        sockectFactory = new SSLSocketFactory(sslContext, SSLSocketFactory.ALLOW_ALL_HOSTNAME_VERIFIER);

        // clean data
        configRepository.deleteAll();
        userRepository.deleteAll();
        roleRepository.deleteAll();

        conf1 = new Config();
        conf1.setConfigName("BLOG_URL");
        conf1.setConfigValue("http://blog.ik.am");
        conf1.setCreatedBy("admin");
        conf1.setCreatedDate(now);
        conf1.setLastModifiedBy("admin");
        conf1.setLastModifiedDate(now);
        conf1.setVersion(1L);

        conf2 = new Config();
        conf2.setConfigName("BLOG_TITLE");
        conf2.setConfigValue("BLOG.IK.AM");
        conf2.setCreatedBy("admin");
        conf2.setCreatedDate(now);
        conf2.setLastModifiedBy("admin");
        conf2.setLastModifiedDate(now);
        conf2.setVersion(1L);

        configRepository.save(Arrays.asList(conf1, conf2));

        Role adminRole = new Role(100, "ADMIN", null);
        Role editorRole = new Role(200, "EDITOR", null);
        roleRepository.save(Arrays.asList(adminRole, editorRole));
        roleRepository.flush();

        admin = new User("admin", passwordEncoder.encode("demo"), "admin@a.b", true, false, "Tarou", "Yamada", Sets.newHashSet(roleRepository.findOneByRoleName("ADMIN")));
        userRepository.save(admin);
        userRepository.flush();

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
    public void testGetConfigs() throws Exception {
        given()
                .log().all()
                .when()
                .get("/api/v1/configs")
                .then()
                .log().all()
                .statusCode(HttpStatus.OK.value())
                .body("[0].configName", is(conf1.getConfigName()))
                .body("[0].configValue", is(conf1.getConfigValue()))
                .body("[1].configName", is(conf2.getConfigName()))
                .body("[1].configValue", is(conf2.getConfigValue()));
    }


    @Test
    public void testPostConfigs() throws Exception {
        ConfigResource input = new ConfigResource("TEST", "test");
        String user = "admin";
        String accessToken = getAccessToken(user, "demo");
        given()
                .log().all()
                .header("X-Admin", "true")
                .header("Authorization", "Bearer " + accessToken)
                .contentType(ContentType.JSON)
                .body(input)
                .when()
                .post("/api/v1/configs")
                .then()
                .log().all()
                .statusCode(HttpStatus.CREATED.value())
                .body("configName", is(input.getConfigName()))
                .body("configValue", is(input.getConfigValue()));

        given()
                .log().all()
                .when()
                .get("/api/v1/configs")
                .then()
                .log().all()
                .statusCode(HttpStatus.OK.value())
                .body("[0].configName", is(conf1.getConfigName()))
                .body("[0].configValue", is(conf1.getConfigValue()))
                .body("[1].configName", is(conf2.getConfigName()))
                .body("[1].configValue", is(conf2.getConfigValue()))
                .body("[2].configName", is(input.getConfigName()))
                .body("[2].configValue", is(input.getConfigValue()));
    }

    @Test
    public void testPostConfig_AlreadyExists() throws Exception {
        ConfigResource input = new ConfigResource("BLOG_TITLE", "test");
        String user = "admin";
        String accessToken = getAccessToken(user, "demo");
        given()
                .log().all()
                .header("X-Admin", "true")
                .header("Authorization", "Bearer " + accessToken)
                .contentType(ContentType.JSON)
                .body(input)
                .when()
                .post("/api/v1/configs")
                .then()
                .log().all()
                .statusCode(HttpStatus.CONFLICT.value())
                .body("code", is(MessageKeys.E_CT_CF_8501))
                .body("message", is("The requested config is already registered. [configName=BLOG_TITLE]"));
    }

    @Test
    public void testPutConfig() throws Exception {
        ConfigResource input = new ConfigResource("BLOG_TITLE", "test");
        String user = "admin";
        String accessToken = getAccessToken(user, "demo");
        given()
                .log().all()
                .header("X-Admin", "true")
                .header("Authorization", "Bearer " + accessToken)
                .contentType(ContentType.JSON)
                .body(input)
                .when()
                .put("/api/v1/configs/{configName}", input.getConfigName())
                .then()
                .log().all()
                .statusCode(HttpStatus.OK.value())
                .body("configName", is(input.getConfigName()))
                .body("configValue", is(input.getConfigValue()));

        given()
                .log().all()
                .when()
                .get("/api/v1/configs")
                .then()
                .log().all()
                .statusCode(HttpStatus.OK.value())
                .body("[0].configName", is(conf1.getConfigName()))
                .body("[0].configValue", is(conf1.getConfigValue()))
                .body("[1].configName", is(conf2.getConfigName()))
                .body("[1].configValue", is(input.getConfigValue()));
    }


    @Test
    public void testPutConfig_NotFound() throws Exception {
        ConfigResource input = new ConfigResource("HOGE", "test");
        String user = "admin";
        String accessToken = getAccessToken(user, "demo");
        given()
                .log().all()
                .header("X-Admin", "true")
                .header("Authorization", "Bearer " + accessToken)
                .contentType(ContentType.JSON)
                .body(input)
                .when()
                .put("/api/v1/configs/{configName}", input.getConfigName())
                .then()
                .log().all()
                .statusCode(HttpStatus.NOT_FOUND.value())
                .body("code", is(MessageKeys.E_CT_CF_8502))
                .body("message", is("The requested config is not found. [configName=HOGE]"));
    }

    @Test
    public void testDeleteConfig() throws Exception {
        String user = "admin";
        String accessToken = getAccessToken(user, "demo");
        given()
                .log().all()
                .header("X-Admin", "true")
                .header("Authorization", "Bearer " + accessToken)
                .when()
                .delete("/api/v1/configs/{configName}", "BLOG_URL")
                .then()
                .log().all()
                .statusCode(HttpStatus.NO_CONTENT.value());
        given()
                .log().all()
                .when()
                .get("/api/v1/configs")
                .then()
                .log().all()
                .statusCode(HttpStatus.OK.value())
                .body("[0].configName", is(conf2.getConfigName()))
                .body("[0].configValue", is(conf2.getConfigValue()));
    }

    @Test
    public void testDeleteConfig_NotFound() throws Exception {
        String user = "admin";
        String accessToken = getAccessToken(user, "demo");
        given()
                .log().all()
                .header("X-Admin", "true")
                .header("Authorization", "Bearer " + accessToken)
                .when()
                .delete("/api/v1/configs/{configName}", "HOGE")
                .then()
                .log().all()
                .statusCode(HttpStatus.NOT_FOUND.value())
                .body("code", is(MessageKeys.E_CT_CF_8502))
                .body("message", is("The requested config is not found. [configName=HOGE]"));
    }
}