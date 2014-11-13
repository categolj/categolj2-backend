package am.ik.categolj2.config;

import lombok.Data;
import org.eclipse.jetty.server.Connector;
import org.eclipse.jetty.server.HttpConfiguration;
import org.eclipse.jetty.server.HttpConnectionFactory;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.server.ServerConnector;
import org.eclipse.jetty.util.ssl.SslContextFactory;
import org.springframework.boot.context.embedded.EmbeddedServletContainerFactory;
import org.springframework.boot.context.embedded.Ssl;
import org.springframework.boot.context.embedded.jetty.JettyEmbeddedServletContainerFactory;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConfigurationProperties("server-http")
@Data
public class EmbeddedServerConfig {
    private int port = 8080;
    private String host;

    @Bean
    EmbeddedServletContainerFactory servletContainer() {
        JettyEmbeddedServletContainerFactory jetty = new JettyEmbeddedServletContainerFactory() {
            @Override
            protected void configureSsl(SslContextFactory factory, Ssl ssl) {
                super.configureSsl(factory, ssl);
                factory.addExcludeProtocols("SSLv2Hello", "SSLv3");
            }
        };
        jetty.addServerCustomizers(server -> server.addConnector(createHttpConnector(server)));
        return jetty;
    }

    Connector createHttpConnector(Server server) {
        HttpConfiguration config = new HttpConfiguration();
        config.setOutputBufferSize(32768);
        config.setRequestHeaderSize(8192);
        config.setResponseHeaderSize(8192);
        ServerConnector http = new ServerConnector(server,new HttpConnectionFactory(config));
        http.setHost(host);
        http.setPort(port);
        http.setIdleTimeout(30000);
        server.addConnector(http);
        return http;
    }
}
