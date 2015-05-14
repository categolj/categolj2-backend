package am.ik.categolj2.config;

import am.ik.categolj2.thrift.TCategolj2;
import org.apache.thrift.protocol.TBinaryProtocol;
import org.apache.thrift.protocol.TJSONProtocol;
import org.apache.thrift.protocol.TProtocolFactory;
import org.apache.thrift.server.TServlet;
import org.springframework.boot.context.embedded.ServletRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;

@Configuration
public class ThriftConfig {

    @Bean
    TProtocolFactory protocolFactory() {
        return new TBinaryProtocol.Factory();
    }

    @Bean
    @Order(Ordered.HIGHEST_PRECEDENCE)
    ServletRegistrationBean thriftHandlerServlet(TCategolj2.Iface thriftHandler) {
        TServlet servlet = new TServlet(new TCategolj2.Processor<>(thriftHandler), protocolFactory());
        ServletRegistrationBean registrationBean =  new ServletRegistrationBean(servlet, "/thrift");
        return registrationBean;
    }
}
