package am.ik.categolj2.config;

import am.ik.categolj2.api.MediaTypeResolver;
import am.ik.categolj2.core.web.accesslog.AccessLogMethodArgumentResolver;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.MediaType;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

import javax.inject.Inject;
import java.util.LinkedHashMap;
import java.util.List;

@Configuration
public class WebConfig extends WebMvcConfigurerAdapter {
    @Inject
    ObjectMapper objectMapper;

    @Bean
    MediaTypeResolver mediaTypeResolver() {
        MediaTypeResolver mediaTypeResolver = new MediaTypeResolver(new LinkedHashMap<String, MediaType>() {{
            put("json", MediaType.APPLICATION_JSON);
            put("xml", MediaType.APPLICATION_XML);
            put("gif", MediaType.IMAGE_GIF);
            put("jpeg", MediaType.IMAGE_JPEG);
            put("png", MediaType.IMAGE_PNG);
            put("html", MediaType.TEXT_HTML);
            put("text", MediaType.TEXT_PLAIN);
            put("xhtml", MediaType.TEXT_XML);
        }});
        mediaTypeResolver.setFallbackMediaType(MediaType.APPLICATION_OCTET_STREAM);
        return mediaTypeResolver;
    }

    @Bean
    AccessLogMethodArgumentResolver accessLogMethodArgumentResolver() {
        return new AccessLogMethodArgumentResolver();
    }

    @Override
    public void addArgumentResolvers(List<HandlerMethodArgumentResolver> argumentResolvers) {
        argumentResolvers.add(accessLogMethodArgumentResolver());
    }
}
