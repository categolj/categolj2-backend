package am.ik.categolj2.config;

import am.ik.aws.apa.AwsApaRequester;
import am.ik.aws.apa.AwsApaRequesterImpl;
import net.sf.log4jdbc.sql.jdbcapi.DataSourceSpy;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.jdbc.DataSourceBuilder;
import org.springframework.boot.autoconfigure.jdbc.DataSourceProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.inject.Inject;
import javax.sql.DataSource;
import java.net.URI;
import java.net.URISyntaxException;

@Configuration
public class InfraConfig {
    @Inject
    DataSourceProperties dataSourceProperties;
    DataSource dataSource;

    @Bean
    DataSource realDataSource() throws URISyntaxException {
        String url;
        String username;
        String password;

        String databaseUrl = System.getenv("DATABASE_URL");
        if (databaseUrl != null) {
            URI dbUri = new URI(databaseUrl);
            url = "jdbc:postgresql://" + dbUri.getHost() + ":" + dbUri.getPort() + dbUri.getPath();
            username = dbUri.getUserInfo().split(":")[0];
            password = dbUri.getUserInfo().split(":")[1];
        } else {
            url = this.dataSourceProperties.getUrl();
            username = this.dataSourceProperties.getUsername();
            password = this.dataSourceProperties.getPassword();
        }

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

    @Bean
    AwsApaRequester apaRequester(@Value("${aws.accesskey.id}") String accessKeyId,
                                 @Value("${aws.secret.accesskey}") String secretAccessKey,
                                 @Value("${aws.endpoint}") String endPoint,
                                 @Value("${aws.associate.tag}") String associateTag) {
        return new AwsApaRequesterImpl(endPoint, accessKeyId, secretAccessKey, associateTag);
    }
}
