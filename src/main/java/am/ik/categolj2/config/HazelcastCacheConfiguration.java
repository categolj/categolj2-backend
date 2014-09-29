package am.ik.categolj2.config;

import com.hazelcast.config.*;
import com.hazelcast.core.Hazelcast;
import com.hazelcast.core.HazelcastInstance;
import com.hazelcast.spring.cache.HazelcastCacheManager;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.CachingConfigurer;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cache.interceptor.KeyGenerator;
import org.springframework.cache.interceptor.SimpleKeyGenerator;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

import javax.inject.Inject;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

@Configuration
@EnableCaching
@Profile("cache.hazelcast")
public class HazelcastCacheConfiguration implements CachingConfigurer {
    @Inject
    HazelcastProperties hazelcastProperties;

    @Bean
    HazelcastInstance hazelcastInstance() {
        MulticastConfig multicastConfig = new MulticastConfig();
        multicastConfig.setEnabled(hazelcastProperties.isMulticastEnabled());
        multicastConfig.setMulticastGroup(hazelcastProperties.getMulticastGroup());
        multicastConfig.setMulticastPort(hazelcastProperties.getMulticastPort());

        JoinConfig joinConfig = new JoinConfig();
        joinConfig.setMulticastConfig(multicastConfig);

        NetworkConfig networkConfig = new NetworkConfig();
        networkConfig.setPort(hazelcastProperties.getPort());
        networkConfig.setPortAutoIncrement(true);
        networkConfig.setJoin(joinConfig);


        NearCacheConfig nearCacheConfig = new NearCacheConfig();
        nearCacheConfig.setMaxSize(hazelcastProperties.getNearCacheMaxSize());
        nearCacheConfig.setMaxIdleSeconds(hazelcastProperties.getNearCacheMaxIdleSeconds());
        nearCacheConfig.setEvictionPolicy("LRU");
        nearCacheConfig.setCacheLocalEntries(true);

        MapConfig mapConfig = new MapConfig();
        mapConfig.setName("categolj");
        mapConfig.setBackupCount(hazelcastProperties.getBackupCount());
        mapConfig.setNearCacheConfig(nearCacheConfig);

        Map<String, MapConfig> mapConfigMap = new HashMap<>();
        mapConfigMap.put(mapConfig.getName(), mapConfig);

        Config config = new Config();
        config.setGroupConfig(new GroupConfig(
                hazelcastProperties.getGroupName(),
                hazelcastProperties.getGroupPassword()));
        config.setProperties(new Properties() {{
            put("hazelcast.jmx", hazelcastProperties.isJmx());
            put("hazelcast.logging.type", "slf4j");
        }});
        config.setNetworkConfig(networkConfig);
        config.setMapConfigs(mapConfigMap);
        return Hazelcast.newHazelcastInstance(config);
    }

    @Bean
    @Override
    public CacheManager cacheManager() {
        HazelcastCacheManager cacheManager = new HazelcastCacheManager(hazelcastInstance());
        return cacheManager;
    }

    @Bean
    @Override
    public KeyGenerator keyGenerator() {
        return new SimpleKeyGenerator();
    }
}
