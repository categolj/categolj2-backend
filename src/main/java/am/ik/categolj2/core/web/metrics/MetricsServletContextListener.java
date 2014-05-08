package am.ik.categolj2.core.web.metrics;

import com.codahale.metrics.MetricRegistry;
import com.codahale.metrics.servlets.MetricsServlet;
import org.springframework.context.ApplicationContext;
import org.springframework.web.context.support.WebApplicationContextUtils;

import javax.servlet.ServletContext;
import javax.servlet.ServletContextEvent;


public class MetricsServletContextListener extends MetricsServlet.ContextListener {
    private ApplicationContext applicationContext;

    @Override
    public void contextInitialized(ServletContextEvent event) {
        final ServletContext context = event.getServletContext();
        this.applicationContext = WebApplicationContextUtils.getRequiredWebApplicationContext(context);
        super.contextInitialized(event);
    }

    @Override
    protected MetricRegistry getMetricRegistry() {
        return applicationContext.getBean(MetricRegistry.class);
    }
}
