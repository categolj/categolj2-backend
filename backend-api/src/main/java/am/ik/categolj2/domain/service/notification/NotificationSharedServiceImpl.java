package am.ik.categolj2.domain.service.notification;

import com.google.common.collect.HashMultimap;
import com.google.common.collect.Multimap;
import com.google.common.collect.Multimaps;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.http.RequestEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import javax.inject.Inject;
import java.net.URI;
import java.net.URISyntaxException;

@Service
@Slf4j
public class NotificationSharedServiceImpl implements NotificationSharedService {

    static final Multimap<String, String> subscribers = Multimaps.synchronizedMultimap(HashMultimap.create());

    @Inject
    RestTemplate restTemplate;
    @Value("${messagingApiKey:}")
    String messagingApiKey;

    @Override
    public void subscribe(SubscribeRequest request) {
        log.info("add subscriber {}", request);
        subscribers.put(request.getEndpoint(), request.getSubscriptionId());
    }

    @Override
    @Async
    public void ping() {
        if (messagingApiKey == null || messagingApiKey.isEmpty()) {
            return;
        }
        subscribers.asMap().entrySet().forEach(
                x -> {
                    log.info("ping {}", x);
                    try {
                        RequestEntity<NotificationRequest> req =
                                RequestEntity.post(new URI(x.getKey()))
                                        .contentType(MediaType.APPLICATION_JSON)
                                        .header("Authorization", "key=" + messagingApiKey)
                                        .body(new NotificationRequest(x.getValue()));
                        ResponseEntity<String> res = restTemplate.exchange(req, String.class);
                        System.out.println(res);
                    } catch (URISyntaxException e) {
                        log.error("URI Syntax Error! -> " + x, e);
                    }
                }
        );
    }
}
