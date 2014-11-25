package am.ik.categolj2.config;

import lombok.Data;
import org.eclipse.jetty.server.Connector;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.server.nio.SelectChannelConnector;
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
        jetty.addServerCustomizers(server -> server.addConnector(createSslConnector(server)));
        return jetty;
    }

    Connector createSslConnector(Server server) {
        Connector connector = new SelectChannelConnector();
        connector.setHost(host);
        connector.setPort(port);
        server.addConnector(connector);
        return connector;
    }
}
