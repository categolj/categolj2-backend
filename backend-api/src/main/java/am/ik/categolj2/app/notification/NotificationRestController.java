package am.ik.categolj2.app.notification;

import am.ik.categolj2.domain.service.notification.NotificationSharedService;
import am.ik.categolj2.domain.service.notification.SubscribeRequest;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.inject.Inject;

@RestController
@RequestMapping("/notification")
public class NotificationRestController {
    @Inject
    NotificationSharedService notificationSharedService;

    @RequestMapping(value = "subscribe", method = RequestMethod.POST)
    String subscribe(@RequestBody @Validated SubscribeRequest request) {
        notificationSharedService.subscribe(request);
        return "OK";
    }
}
