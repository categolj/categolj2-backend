package am.ik.categolj2.config;

import am.ik.categolj2.domain.service.token.Categolj2TokenEnhancer;
import am.ik.categolj2.infra.token.CachingTokenStore;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.oauth2.provider.ClientDetailsService;
import org.springframework.security.oauth2.provider.InMemoryClientDetailsService;
import org.springframework.security.oauth2.provider.token.DefaultTokenServices;
import org.springframework.security.oauth2.provider.token.JdbcTokenStore;
import org.springframework.security.oauth2.provider.token.TokenStore;

import javax.inject.Inject;
import javax.sql.DataSource;
import java.util.LinkedHashMap;

@Configuration
public class OAuth2Config {
    @Inject
    DataSource dataSource;

    @Bean
    ClientDetailsService clientDetailsService() {
        InMemoryClientDetailsService clientDetailsService = new InMemoryClientDetailsService();
        clientDetailsService.setClientDetailsStore(new LinkedHashMap<>());
        return clientDetailsService;
    }

    @Bean
    TokenStore tokenStore() {
        return new CachingTokenStore(new JdbcTokenStore(dataSource));
    }

    @Bean
    DefaultTokenServices tokenServices() {
        DefaultTokenServices tokenServices = new DefaultTokenServices();
        tokenServices.setTokenStore(tokenStore());
        tokenServices.setClientDetailsService(clientDetailsService());
        tokenServices.setTokenEnhancer(new Categolj2TokenEnhancer());
        tokenServices.setSupportRefreshToken(true);
        return tokenServices;
    }
}
