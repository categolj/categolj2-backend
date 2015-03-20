package am.ik.categolj2.domain.service.config;

import am.ik.categolj2.domain.model.Config;

import java.util.List;

public interface ConfigService {
    List<Config> findAll();

    Config create(String propertyName, String propertyValue);

    Config update(String propertyName, String propertyValue);

    void delete(String propertyName);
}
