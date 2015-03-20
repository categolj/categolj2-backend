package am.ik.categolj2.domain.service.config;

import am.ik.categolj2.core.message.MessageKeys;
import am.ik.categolj2.domain.model.Config;
import am.ik.categolj2.domain.repository.config.ConfigRepository;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.terasoluna.gfw.common.exception.BusinessException;
import org.terasoluna.gfw.common.exception.ResourceNotFoundException;
import org.terasoluna.gfw.common.message.ResultMessages;

import javax.inject.Inject;
import java.util.List;

@Service
@Transactional
public class ConfigServiceImpl implements ConfigService {
    @Inject
    ConfigRepository configRepository;

    @Override
    @Transactional(readOnly = true)
    @Cacheable("configs")
    public List<Config> findAll() {
        return configRepository.findAll();
    }

    @Override
    @CacheEvict(value = "configs", allEntries = true)
    public Config create(String propertyName, String propertyValue) {
        if (configRepository.exists(propertyName)) {
            throw new BusinessException(ResultMessages.error().add(MessageKeys.E_CT_CF_8501, propertyName));
        }

        return configRepository.save(new Config(propertyName, propertyValue));
    }

    @Override
    @CacheEvict(value = "configs", allEntries = true)
    public Config update(String propertyName, String propertyValue) {
        if (!configRepository.exists(propertyName)) {
            throw new ResourceNotFoundException(ResultMessages.error().add(MessageKeys.E_CT_CF_8502, propertyName));
        }
        Config config = configRepository.findOne(propertyName);
        config.setConfigValue(propertyValue);
        return config;
    }

    @Override
    @CacheEvict(value = "configs", allEntries = true)
    public void delete(String propertyName) {
        if (!configRepository.exists(propertyName)) {
            throw new ResourceNotFoundException(ResultMessages.error().add(MessageKeys.E_CT_CF_8502, propertyName));
        }
        configRepository.delete(propertyName);
    }
}
