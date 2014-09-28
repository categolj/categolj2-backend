package am.ik.categolj2.config;

import am.ik.categolj2.core.message.MessageKeys;
import am.ik.categolj2.domain.model.EntryFormat;
import am.ik.categolj2.infra.codelist.EnumCodeList;
import ch.qos.logback.classic.Level;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.exc.UnrecognizedPropertyException;
import org.dozer.spring.DozerBeanMapperFactoryBean;
import org.springframework.beans.TypeMismatchException;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
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
import org.terasoluna.gfw.common.codelist.CodeList;
import org.terasoluna.gfw.common.codelist.SimpleMapCodeList;
import org.terasoluna.gfw.common.date.DateFactory;
import org.terasoluna.gfw.common.date.DefaultDateFactory;
import org.terasoluna.gfw.common.exception.*;
import org.terasoluna.gfw.web.exception.ExceptionLoggingFilter;
import org.terasoluna.gfw.web.logging.mdc.MDCClearFilter;
import org.terasoluna.gfw.web.logging.mdc.XTrackMDCPutFilter;

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

    @Bean
    ExceptionLogger exceptionLogger() {
        ExceptionLogger exceptionLogger = new ExceptionLogger();
        exceptionLogger.setExceptionCodeResolver(exceptionCodeResolver());
        return exceptionLogger;
    }

    @Bean
    ExceptionLoggingFilter exceptionLoggingFilter() {
        ExceptionLoggingFilter exceptionLoggingFilter = new ExceptionLoggingFilter();
        exceptionLoggingFilter.setExceptionLogger(exceptionLogger());
        return exceptionLoggingFilter;
    }

    @Order(Ordered.HIGHEST_PRECEDENCE + 3)
    @Bean
    XTrackMDCPutFilter xTrackMDCPutFilter() {
        return new XTrackMDCPutFilter();
    }

    @Order(Ordered.HIGHEST_PRECEDENCE + 2)
    @Bean
    MDCClearFilter mdcClearFilter() {
        return new MDCClearFilter();
    }

    // Codelist
    @Bean(name = "CL_FORMAT")
    CodeList formatCodeList() {
        EnumCodeList codeList = new EnumCodeList();
        codeList.setEnumClass(EntryFormat.class);
        return codeList;
    }

    @Bean(name = "CL_LOGGER_LEVEL")
    CodeList loggerLevelCodeList() {
        SimpleMapCodeList codeList = new SimpleMapCodeList();
        codeList.setMap(new LinkedHashMap<String, String>() {{
            put(Level.OFF.toString(), Level.OFF.toString());
            put(Level.ERROR.toString(), Level.ERROR.toString());
            put(Level.WARN.toString(), Level.WARN.toString());
            put(Level.INFO.toString(), Level.INFO.toString());
            put(Level.DEBUG.toString(), Level.DEBUG.toString());
            put(Level.TRACE.toString(), Level.TRACE.toString());
        }});
        return codeList;
    }

}
