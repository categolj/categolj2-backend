package am.ik.categolj2.api.config;

import am.ik.categolj2.api.ApiVersion;
import am.ik.categolj2.api.Categolj2Headers;
import am.ik.categolj2.domain.model.Config;
import am.ik.categolj2.domain.service.config.ConfigService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.inject.Inject;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("api/" + ApiVersion.CURRENT_VERSION + "/configs")
public class ConfigRestController {
    @Inject
    ConfigService configService;

    @RequestMapping(method = RequestMethod.GET)
    public List<ConfigResource> getConfigs() {
        return configService.findAll().stream()
                .map(config -> new ConfigResource(config.getConfigName(), config.getConfigValue()))
                .collect(Collectors.toList());
    }

    @RequestMapping(method = RequestMethod.POST, headers = Categolj2Headers.X_ADMIN)
    public ResponseEntity<ConfigResource> postConfigs(@RequestBody ConfigResource config) {
        Config conf = configService.create(config.getConfigName(), config.getConfigValue());
        return ResponseEntity.status(HttpStatus.CREATED).body(new ConfigResource(conf.getConfigName(), conf.getConfigValue()));
    }

    @RequestMapping(value = "{configName}", method = RequestMethod.PUT, headers = Categolj2Headers.X_ADMIN)
    public ConfigResource putConfig(@PathVariable("configName") String configName, @RequestBody Config config) {
        Config conf = configService.update(configName, config.getConfigValue());
        return new ConfigResource(conf.getConfigName(), conf.getConfigValue());
    }

    @RequestMapping(value = "{configName}", method = RequestMethod.DELETE, headers = Categolj2Headers.X_ADMIN)
    public ResponseEntity<?> deleteConfig(@PathVariable("configName") String configName) {
        configService.delete(configName);
        return ResponseEntity.noContent().build();
    }
}
