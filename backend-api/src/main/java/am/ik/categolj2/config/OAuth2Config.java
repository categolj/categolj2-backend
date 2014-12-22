package am.ik.categolj2.config;

import am.ik.categolj2.domain.service.token.Categolj2TokenEnhancer;
import am.ik.categolj2.infra.token.CachingTokenStore;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.oauth2.config.annotation.builders.InMemoryClientDetailsServiceBuilder;
import org.springframework.security.oauth2.config.annotation.configurers.ClientDetailsServiceConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configuration.AuthorizationServerConfigurerAdapter;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableAuthorizationServer;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableResourceServer;
import org.springframework.security.oauth2.config.annotation.web.configuration.ResourceServerConfigurerAdapter;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerEndpointsConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerSecurityConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configurers.ResourceServerSecurityConfigurer;
import org.springframework.security.oauth2.provider.ClientDetailsService;
import org.springframework.security.oauth2.provider.token.DefaultTokenServices;
import org.springframework.security.oauth2.provider.token.TokenStore;
import org.springframework.security.oauth2.provider.token.store.JdbcTokenStore;

import javax.inject.Inject;
import javax.sql.DataSource;

@Configuration
public class OAuth2Config {
    private static final String RESOURCE_ID = "categolj2";

    @Configuration
    @EnableResourceServer
    protected static class ResourceServerConfiguration extends
            ResourceServerConfigurerAdapter {

        @Override
        public void configure(ResourceServerSecurityConfigurer resources) {
            resources.resourceId(RESOURCE_ID);
        }

        @Override
        public void configure(HttpSecurity http) throws Exception {
            http.sessionManagement()
                    .sessionCreationPolicy(SessionCreationPolicy.STATELESS);
            http.authorizeRequests()
                    .antMatchers(HttpMethod.POST, "/api/**").authenticated()
                    .antMatchers(HttpMethod.PUT, "/api/**").authenticated()
                    .antMatchers(HttpMethod.DELETE, "/api/**").authenticated()
                    .antMatchers(HttpMethod.OPTIONS, "/management/**").permitAll()
                    .antMatchers("/management/**").authenticated();
            http.requiresChannel()
                    .antMatchers("/login**").requiresSecure();
            http.headers().disable();
        }
    }

    @Configuration
    @EnableAuthorizationServer
    protected static class AuthorizationServerConfiguration extends
            AuthorizationServerConfigurerAdapter {
        @Inject
        DataSource dataSource;
        @Inject
        AuthenticationManager authenticationManager;
        @Inject
        OAuth2AdminClientProperties adminClientProperties;

        @Override
        public void configure(AuthorizationServerEndpointsConfigurer endpoints)
                throws Exception {
            endpoints
                    .tokenServices(tokenServices())
                    .tokenStore(tokenStore())
                    .authenticationManager(authenticationManager);
        }

        @Override
        public void configure(ClientDetailsServiceConfigurer clients) throws Exception {
            clients.withClientDetails(clientDetailsService());
        }

        @Override
        public void configure(AuthorizationServerSecurityConfigurer oauthServer) throws Exception {
            oauthServer.realm(RESOURCE_ID);
        }

        @Bean
        ClientDetailsService clientDetailsService() throws Exception {
            InMemoryClientDetailsServiceBuilder builder = new InMemoryClientDetailsServiceBuilder();
            builder.withClient(adminClientProperties.getClientId())
                    .authorizedGrantTypes("password", "refresh_token")
                    .authorities("ROLE_CLIENT", "ROLE_TRUSTED_CLIENT")
                    .scopes("read", "write")
                    .resourceIds(RESOURCE_ID)
                    .secret(adminClientProperties.getClientSecret());
            ClientDetailsService clientDetailsService = builder.build();
            return clientDetailsService;
        }

        @Bean
        TokenStore tokenStore() {
            return new CachingTokenStore(new JdbcTokenStore(dataSource));
        }

        @Bean
        DefaultTokenServices tokenServices() throws Exception {
            DefaultTokenServices tokenServices = new DefaultTokenServices();
            tokenServices.setTokenStore(tokenStore());
            tokenServices.setClientDetailsService(clientDetailsService());
            tokenServices.setTokenEnhancer(new Categolj2TokenEnhancer());
            tokenServices.setSupportRefreshToken(true);
            return tokenServices;
        }
    }
}
