package am.ik.categolj2.config;

import lombok.Data;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Data
@Configuration
@ConfigurationProperties(prefix = "categolj2.admin")
public class Categolj2AdminProperties {
    private String clientId;
    private String clientSecret;
    boolean forceHttps = true;
}
