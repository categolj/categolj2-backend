package am.ik.categolj2.config;

import am.ik.categolj2.infra.cache.LoggingGuavaCacheManager;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.CachingConfigurer;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cache.interceptor.KeyGenerator;
import org.springframework.cache.interceptor.SimpleKeyGenerator;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

import java.util.Arrays;

@Configuration
@EnableCaching
@Profile("cache.guava")
public class GuavaCacheConfiguration implements CachingConfigurer {
    @Bean
    @Override
    public CacheManager cacheManager() {
        LoggingGuavaCacheManager cacheManager = new LoggingGuavaCacheManager();
        cacheManager.setCacheSpecification("maximumSize=10000,expireAfterWrite=3d");
        cacheManager.setCacheNames(Arrays.asList(
                "entry",
                "recentPost",
                "link",
                "uploadFileSummary",
                "accessToken",
                "accessTokenAuthentication"));
        return cacheManager;
    }

    @Bean
    @Override
    public KeyGenerator keyGenerator() {
        return new SimpleKeyGenerator();
    }
}
