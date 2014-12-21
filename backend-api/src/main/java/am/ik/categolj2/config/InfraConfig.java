package am.ik.categolj2.config;

import am.ik.aws.apa.AwsApaRequester;
import am.ik.aws.apa.AwsApaRequesterImpl;
import am.ik.categolj2.domain.AuditAwareBean;
import am.ik.categolj2.infra.db.UrlStringDevider;
import net.sf.log4jdbc.sql.jdbcapi.DataSourceSpy;
import org.flywaydb.core.Flyway;
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

@Configuration
@EnableJpaAuditing(setDates = false)
public class InfraConfig {

    @Bean
    AuditAwareBean auditAwareBean() {
        return new AuditAwareBean();
    }

    @Configuration
    @Profile("db.property")
    public static class PropertyDbConfiguration {
        @Inject
        DataSourceProperties dataSourceProperties;
        DataSource dataSource;

        @Bean
        DataSource realDataSource() throws URISyntaxException {
            String url = this.dataSourceProperties.getUrl();
            String username = this.dataSourceProperties.getUsername();
            String password = this.dataSourceProperties.getPassword();

            DataSourceBuilder factory = DataSourceBuilder
                    .create(this.dataSourceProperties.getClassLoader())
                    .url(url)
                    .username(username)
                    .password(password);
            this.dataSource = factory.build();
            return this.dataSource;
        }

        @Bean
        DataSource dataSource() {
            return new DataSourceSpy(this.dataSource);
        }
    }


    @Configuration
    @Profile("db.urlstring")
    public static class UrlStringDbConfiguration {
        DataSource dataSource;

        @Bean
        DataSource realDataSource() throws URISyntaxException {
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
            this.dataSource = factory.build();
            return this.dataSource;
        }

        @Bean
        DataSource dataSource() {
            return new DataSourceSpy(this.dataSource);
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
