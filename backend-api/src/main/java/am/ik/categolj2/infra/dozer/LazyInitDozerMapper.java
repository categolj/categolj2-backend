package am.ik.categolj2.infra.dozer;


import lombok.extern.slf4j.Slf4j;
import org.dozer.DozerBeanMapper;
import org.dozer.Mapper;
import org.dozer.MappingException;

import javax.annotation.PreDestroy;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Function;

@Slf4j
public class LazyInitDozerMapper implements Mapper {
    private final Function<Class, Mapper> mapperProvider;
    private final ConcurrentHashMap<Class, Mapper> maperCache = new ConcurrentHashMap<>(1);

    public LazyInitDozerMapper(Function<Class, Mapper> mapperProvider) {
        this.mapperProvider = mapperProvider;
    }

    @Override
    public <T> T map(Object source, Class<T> destinationClass) throws MappingException {
        return this.maperCache.computeIfAbsent(Mapper.class, this.mapperProvider::apply).map(source, destinationClass);
    }

    @Override
    public void map(Object source, Object destination) throws MappingException {
        this.maperCache.computeIfAbsent(Mapper.class, this.mapperProvider::apply).map(source, destination);
    }

    @Override
    public <T> T map(Object source, Class<T> destinationClass, String mapId) throws MappingException {
        return this.maperCache.computeIfAbsent(Mapper.class, this.mapperProvider::apply).map(source, destinationClass, mapId);
    }

    @Override
    public void map(Object source, Object destination, String mapId) throws MappingException {
        this.maperCache.computeIfAbsent(Mapper.class, this.mapperProvider::apply).map(source, destination, mapId);
    }

    @PreDestroy
    public void destroy() {
        Mapper mapper = this.maperCache.get(Mapper.class);
        if (mapper instanceof DozerBeanMapper) {
            log.info("destroy {}", mapper);
            ((DozerBeanMapper) mapper).destroy();
        }
    }
}
