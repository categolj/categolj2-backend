package am.ik.categolj2.thrift;

import am.ik.categolj2.domain.service.entry.EntryService;
import am.ik.categolj2.domain.service.link.LinkService;
import org.apache.thrift.TException;
import org.dozer.Mapper;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Component;
import org.terasoluna.gfw.common.exception.ResourceNotFoundException;
import org.terasoluna.gfw.common.message.ResultMessage;
import org.terasoluna.gfw.common.message.ResultMessageUtils;

import javax.inject.Inject;
import java.util.List;
import java.util.stream.Collectors;

@Component
public class ThriftHandler implements TCategolj2.Iface {
    @Inject
    EntryService entryService;
    @Inject
    LinkService linkService;
    @Inject
    Mapper beanMapper;
    @Inject
    MessageSource messageSource;

    @Override
    public TEntry findOnePublishedEntry(int entryId) throws TException {
        try {
            return beanMapper.map(entryService.findOnePublished(entryId), TEntry.class);
        } catch (ResourceNotFoundException e) {
            ResultMessage message = e.getResultMessages().iterator().next();
            String errorMessage = ResultMessageUtils.resolveMessage(message, messageSource);
            throw new TCategolj2ClientException(message.getCode(), errorMessage);
        }
    }

    @Override
    public List<TEntry> findAllPublishedUpdatedRecently() throws TException {
        return entryService.findAllPublishedUpdatedRecently().stream()
                .map(entry -> beanMapper.map(entry, TEntry.class))
                .collect(Collectors.toList());
    }

    @Override
    public List<TLink> findAllLinks() throws TException {
        return linkService.findAll().stream()
                .map(link -> beanMapper.map(link, TLink.class))
                .collect(Collectors.toList());
    }
}
