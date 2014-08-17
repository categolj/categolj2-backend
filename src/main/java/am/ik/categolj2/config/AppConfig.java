package am.ik.categolj2.config;

import am.ik.categolj2.core.message.MessageKeys;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.exc.UnrecognizedPropertyException;
import org.dozer.spring.DozerBeanMapperFactoryBean;
import org.springframework.beans.TypeMismatchException;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.ResourceArrayPropertyEditor;
import org.springframework.dao.DataAccessException;
import org.springframework.dao.OptimisticLockingFailureException;
import org.springframework.dao.PessimisticLockingFailureException;
import org.springframework.http.converter.json.Jackson2ObjectMapperFactoryBean;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.validation.BindException;
import org.springframework.web.HttpMediaTypeNotAcceptableException;
import org.springframework.web.HttpMediaTypeNotSupportedException;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.client.RestTemplate;
import org.terasoluna.gfw.common.date.DateFactory;
import org.terasoluna.gfw.common.date.DefaultDateFactory;
import org.terasoluna.gfw.common.exception.BusinessException;
import org.terasoluna.gfw.common.exception.ExceptionCodeResolver;
import org.terasoluna.gfw.common.exception.ResourceNotFoundException;
import org.terasoluna.gfw.common.exception.SimpleMappingExceptionCodeResolver;

import java.util.LinkedHashMap;

@Configuration
public class AppConfig {

    @Bean
    DozerBeanMapperFactoryBean dozerMapper() throws Exception {
        DozerBeanMapperFactoryBean factoryBean = new DozerBeanMapperFactoryBean();
        ResourceArrayPropertyEditor editor = new ResourceArrayPropertyEditor();
        editor.setAsText("classpath*:/dozer/**/*.xml");
        factoryBean.setMappingFiles((Resource[]) editor.getValue());
        return factoryBean;
    }

    @Bean
    PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    RestTemplate restTemplate() {
        return new RestTemplate();
    }

    @Bean
    Jackson2ObjectMapperFactoryBean objectMapper() {
        Jackson2ObjectMapperFactoryBean factoryBean = new Jackson2ObjectMapperFactoryBean();
        ObjectMapper objectMapper = new ObjectMapper();
        factoryBean.setObjectMapper(objectMapper);
        factoryBean.setSimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSZ");
        return factoryBean;
    }

    @Bean
    DateFactory dateFactory() {
        return new DefaultDateFactory();
    }

    @Bean
    ExceptionCodeResolver exceptionCodeResolver() {
        SimpleMappingExceptionCodeResolver exceptionCodeResolver = new SimpleMappingExceptionCodeResolver();
        exceptionCodeResolver.setDefaultExceptionCode(MessageKeys.E_CT_FW_9001);
        exceptionCodeResolver.setExceptionMappings(new LinkedHashMap<String, String>() {{
            put(ResourceNotFoundException.class.getSimpleName(), MessageKeys.E_CT_FW_5001);
            put(HttpRequestMethodNotSupportedException.class.getSimpleName(), MessageKeys.E_CT_FW_6001);
            put(HttpMediaTypeNotAcceptableException.class.getSimpleName(), MessageKeys.E_CT_FW_6002);
            put(HttpMediaTypeNotSupportedException.class.getSimpleName(), MessageKeys.E_CT_FW_6003);
            put(MethodArgumentNotValidException.class.getSimpleName(), MessageKeys.E_CT_FW_7001);
            put(BindException.class.getSimpleName(), MessageKeys.E_CT_FW_7002);
            put(JsonParseException.class.getSimpleName(), MessageKeys.E_CT_FW_7003);
            put(UnrecognizedPropertyException.class.getSimpleName(), MessageKeys.E_CT_FW_7004);
            put(TypeMismatchException.class.getSimpleName(), MessageKeys.E_CT_FW_7006);
            put(BusinessException.class.getSimpleName(), MessageKeys.E_CT_FW_8001);
            put(OptimisticLockingFailureException.class.getSimpleName(), MessageKeys.E_CT_FW_8002);
            put(PessimisticLockingFailureException.class.getSimpleName(), MessageKeys.E_CT_FW_8002);
            put(DataAccessException.class.getSimpleName(), MessageKeys.E_CT_FW_9002);
        }});
        return exceptionCodeResolver;
    }
}
