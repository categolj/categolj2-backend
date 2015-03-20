package am.ik.categolj2.app.config;

import am.ik.categolj2.domain.model.Config;
import am.ik.categolj2.domain.service.config.ConfigService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.inject.Inject;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
public class ConfigJsController {
    @Inject
    ConfigService configService;
    @Inject
    ObjectMapper objectMapper;

    @RequestMapping(value = "config.js", method = RequestMethod.GET)
    public ResponseEntity<String> getConfigJs() throws JsonProcessingException {
        Map<String, String> config = configService.findAll().stream()
                .collect(Collectors.toMap(Config::getConfigName, Config::getConfigValue));
        return ResponseEntity.ok()
                .contentType(MediaType.parseMediaType("application/javascript"))
                .body("window.Config = " + objectMapper.writeValueAsString(config) + ";");
    }
}
