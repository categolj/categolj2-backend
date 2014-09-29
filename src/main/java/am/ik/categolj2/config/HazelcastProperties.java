package am.ik.categolj2.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Data
@Configuration
@ConfigurationProperties(prefix = "hazelcast")
public class HazelcastProperties {
    private String groupName = "dev";
    private String groupPassword = "password";
    private boolean jmx = true;
    private int port = 5701;
    private boolean multicastEnabled = true;
    private String multicastGroup = "224.2.2.3";
    private int multicastPort = 54327;
    private int backupCount = 1;
    private int nearCacheMaxSize = 5000;
    private int nearCacheMaxIdleSeconds = 1 * 24 * 60 * 60;
}
