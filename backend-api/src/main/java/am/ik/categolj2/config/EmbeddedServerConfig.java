package am.ik.categolj2.config;

import lombok.Data;
import org.eclipse.jetty.server.Connector;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.server.nio.SelectChannelConnector;
import org.eclipse.jetty.util.ssl.SslContextFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.embedded.EmbeddedServletContainerFactory;
import org.springframework.boot.context.embedded.Ssl;
import org.springframework.boot.context.embedded.jetty.JettyEmbeddedServletContainerFactory;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.util.SocketUtils;

@Configuration
@ConfigurationProperties("server-http")
@Data
public class EmbeddedServerConfig {
    private int port = 8080;
    private String host;
    @Value("${local.server.port:8443}")
    private int httpsPort;

    @Bean
    EmbeddedServletContainerFactory servletContainer() {
        JettyEmbeddedServletContainerFactory jetty = new JettyEmbeddedServletContainerFactory() {
            @Override
            protected void configureSsl(SslContextFactory factory, Ssl ssl) {
                super.configureSsl(factory, ssl);
                factory.addExcludeProtocols("SSLv2Hello", "SSLv3", "TLSv1", "TLSv1.1");
            }
        };
        jetty.addServerCustomizers(server -> server.addConnector(createSslConnector(server)));
        return jetty;
    }

    Connector createSslConnector(Server server) {
        Connector connector = new SelectChannelConnector();
        connector.setHost(host);
        if (port <= 0) {
            do {
                port = SocketUtils.findAvailableTcpPort();
            } while (port == httpsPort);
        }
        connector.setPort(port);
        server.addConnector(connector);
        return connector;
    }
}
