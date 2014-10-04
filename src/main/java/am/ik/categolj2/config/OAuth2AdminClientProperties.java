package am.ik.categolj2.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Data
@Configuration
@ConfigurationProperties(prefix = "categolj2.admin")
public class OAuth2AdminClientProperties {
    private String clientId;
    private String clientSecret;
}
