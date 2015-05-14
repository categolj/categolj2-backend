package am.ik.categolj2.thrift;

import am.ik.categolj2.domain.service.entry.EntryService;
import org.apache.thrift.TException;
import org.dozer.Mapper;
import org.springframework.stereotype.Component;

import javax.inject.Inject;

@Component
public class ThriftHandler implements TCategolj2.Iface {
    @Inject
    EntryService entryService;
    @Inject
    Mapper beanMapper;

    @Override
    public TEntry findOne(int entryId) throws TException {
        return beanMapper.map(entryService.findOne(entryId), TEntry.class);
    }
}
