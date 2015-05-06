package am.ik.categolj2.config;

import am.ik.aws.apa.AwsApaRequester;
import am.ik.aws.apa.AwsApaRequesterImpl;
import am.ik.categolj2.domain.AuditAwareBean;
import am.ik.categolj2.infra.db.ConnectionValidator;
import am.ik.categolj2.infra.db.UrlStringDevider;
import ch.qos.logback.classic.Level;
import ch.qos.logback.classic.LoggerContext;
import lombok.extern.slf4j.Slf4j;
import net.sf.log4jdbc.sql.jdbcapi.DataSourceSpy;
import org.flywaydb.core.Flyway;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.boot.autoconfigure.jdbc.DataSourceBuilder;
import org.springframework.boot.autoconfigure.jdbc.DataSourceProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

import javax.inject.Inject;
import javax.sql.DataSource;
import java.net.URISyntaxException;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.concurrent.TimeUnit;

@Configuration
@EnableJpaAuditing(setDates = false)
@Slf4j
public class InfraConfig {

    @Bean
    AuditAwareBean auditAwareBean() {
        return new AuditAwareBean();
    }

    static void setValidationQuery(DataSource dataSource) {
        if (dataSource instanceof org.apache.tomcat.jdbc.pool.DataSource) {
            org.apache.tomcat.jdbc.pool.DataSource ds = (org.apache.tomcat.jdbc.pool.DataSource) dataSource;
            ds.setValidationQuery("SELECT 1");
            ds.setTestOnBorrow(true);
            ds.setTestWhileIdle(true);
            ds.setValidationInterval(TimeUnit.MINUTES.toMillis(1));
            ds.setTimeBetweenEvictionRunsMillis((int) TimeUnit.MINUTES.toMillis(5));
            ds.setValidatorClassName(ConnectionValidator.class.getName());
        }
    }

    @Configuration
    @Profile("db.property")
    public static class PropertyDbConfiguration {
        @Inject
        DataSourceProperties dataSourceProperties;

        @Bean
        DataSourceBuilder realDataSourceBuilder() throws URISyntaxException {
            String url = this.dataSourceProperties.getUrl();
            String username = this.dataSourceProperties.getUsername();
            String password = this.dataSourceProperties.getPassword();

            DataSourceBuilder factory = DataSourceBuilder
                    .create(this.dataSourceProperties.getClassLoader())
                    .url(url)
                    .username(username)
                    .password(password);
            return factory;
        }

        @Bean
        DataSource dataSource(DataSourceBuilder factory) {
            DataSource dataSource = factory.build();
            setValidationQuery(dataSource);
            return new DataSourceSpy(dataSource);
        }
    }

    @Configuration
    @Profile("db.docker")
    public static class DockerDbConfiguration {
        @Value("#{systemEnvironment['MYSQL_PORT_3306_TCP_ADDR']}")
        String mysqlHost;
        @Value("#{systemEnvironment['MYSQL_PORT_3306_TCP_PORT']}")
        int mysqlPort;
        @Value("${spring.datasource.database:categolj2}")
        String database;
        @Inject
        DataSourceProperties dataSourceProperties;

        @Bean
        DataSourceBuilder realDataSourceBuilder() throws URISyntaxException {
            String url = "jdbc:mysql://" + mysqlHost + ":" + mysqlPort + "/" + database + "?zeroDateTimeBehavior=convertToNull";
            String username = this.dataSourceProperties.getUsername();
            String password = this.dataSourceProperties.getPassword();

            DataSourceBuilder factory = DataSourceBuilder
                    .create()
                    .url(url)
                    .username(username)
                    .password(password);
            return factory;
        }

        @Bean
        DataSource dataSource(DataSourceBuilder factory) {
            DataSource dataSource = factory.build();
            setValidationQuery(dataSource);
            checkDbAvailiability(dataSource);
            return new DataSourceSpy(dataSource);
        }

        void checkDbAvailiability(DataSource dataSource) {
            // ** Hack for Docker Compose **
            // Check Availability because DB could not start yet by Docker Compose
            log.info("Check DB availability... ({}:{})", mysqlHost, mysqlPort);

            LoggerContext context = (LoggerContext) LoggerFactory.getILoggerFactory();
            Level currentLevel = context.getLogger("org.apache.tomcat.jdbc.pool.ConnectionPool").getLevel();
            context.getLogger("org.apache.tomcat.jdbc.pool.ConnectionPool").setLevel(Level.OFF);
            int retryMax = 10;
            for (int i = 0; i < retryMax; i++) {
                if (i > 0) {
                    long sleep = 2 * i;
                    log.info("Retry after {} sec ({}/{})", sleep, i + 1, retryMax);
                    try {
                        TimeUnit.SECONDS.sleep(sleep);
                    } catch (InterruptedException e) {
                        log.info("Interrupted!");
                        Thread.currentThread().interrupt();
                    }
                }
                try (Connection connection = dataSource.getConnection();
                     Statement statement = connection.createStatement()) {
                    statement.execute("SELECT 1");
                    log.info("OK");
                    break;
                } catch (SQLException e) {
                    String message = e.getMessage();
                    if (message != null) {
                        message = message.replace("\n", "").replace("\r", "");
                    }
                    log.info("NG ({})", message);
                    if (i + 1 == retryMax) {
                        throw new IllegalStateException("Failed checking DB availability", e);
                    }
                }
            }
            context.getLogger("org.apache.tomcat.jdbc.pool.ConnectionPool").setLevel(currentLevel);
        }
    }

    @Configuration
    @Profile("db.urlstring")
    public static class UrlStringDbConfiguration {
        DataSource dataSource;

        @Bean
        DataSourceBuilder realDataSourceBuilder() throws URISyntaxException {
            UrlStringDevider urlStringDevider = new UrlStringDevider(
                    System.getenv("CLEARDB_DATABASE_URL"),
                    "zeroDateTimeBehavior=convertToNull");
            String url = urlStringDevider.getUrl();
            String username = urlStringDevider.getUsername();
            String password = urlStringDevider.getPassword();

            DataSourceBuilder factory = DataSourceBuilder
                    .create()
                    .url(url)
                    .username(username)
                    .password(password);
            return factory;
        }

        @Bean
        DataSource dataSource(DataSourceBuilder factory) {
            DataSource dataSource = factory.build();
            setValidationQuery(dataSource);
            return new DataSourceSpy(dataSource);
        }
    }

    @Bean
    BeanPostProcessor flywayBeanPostProcessor() {
        return new BeanPostProcessor() {
            @Override
            public Object postProcessBeforeInitialization(Object bean, String beanName) throws BeansException {
                if (bean instanceof Flyway) {
                    ((Flyway) bean).setValidateOnMigrate(false);
                }
                return bean;
            }

            @Override
            public Object postProcessAfterInitialization(Object bean, String beanName) throws BeansException {
                return bean;
            }
        };
    }

    @Bean
    AwsApaRequester apaRequester(@Value("${aws.accesskey.id}") String accessKeyId,
                                 @Value("${aws.secret.accesskey}") String secretAccessKey,
                                 @Value("${aws.endpoint}") String endPoint,
                                 @Value("${aws.associate.tag}") String associateTag) {
        return new AwsApaRequesterImpl(endPoint, accessKeyId, secretAccessKey, associateTag);
    }

}
