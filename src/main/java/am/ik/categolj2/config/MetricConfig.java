package am.ik.categolj2.config;

import com.codahale.metrics.MetricRegistry;
import com.codahale.metrics.health.HealthCheckRegistry;
import com.codahale.metrics.jvm.*;
import com.codahale.metrics.servlets.MetricsServlet;
import com.ryantenney.metrics.spring.config.annotation.EnableMetrics;
import com.ryantenney.metrics.spring.config.annotation.MetricsConfigurerAdapter;
import org.springframework.boot.context.embedded.ServletRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.lang.management.ManagementFactory;

@EnableMetrics(proxyTargetClass = true)
@Configuration
public class MetricConfig extends MetricsConfigurerAdapter {

    private static final MetricRegistry METRIC_REGISTRY = new MetricRegistry();
    private static final HealthCheckRegistry HEALTH_CHECK_REGISTRY = new HealthCheckRegistry();

    @Override
    public void configureReporters(MetricRegistry metricRegistry) {

    }

    @Override
    public HealthCheckRegistry getHealthCheckRegistry() {
        HealthCheckRegistry healthCheckRegistry = new HealthCheckRegistry();
        return healthCheckRegistry;
    }

    @Override
    public MetricRegistry getMetricRegistry() {
        MetricRegistry metricRegistry = new MetricRegistry();
        metricRegistry.register("jvm.gc", new GarbageCollectorMetricSet());
        metricRegistry.register("jvm.memory", new MemoryUsageGaugeSet());
        metricRegistry.register("jvm.threads", new ThreadStatesGaugeSet());
        metricRegistry.register("jvm.files", new FileDescriptorRatioGauge());
        return metricRegistry;
    }

    @Bean
    ServletRegistrationBean metricServlet(MetricRegistry metricRegistry) {
        return new ServletRegistrationBean(new MetricsServlet(metricRegistry), "/codahale/metrics");
    }

}
