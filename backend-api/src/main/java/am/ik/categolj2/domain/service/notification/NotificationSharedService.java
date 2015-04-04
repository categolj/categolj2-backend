package am.ik.categolj2.domain.service.notification;

public interface NotificationSharedService {
    void subscribe(SubscribeRequest request);

    void ping();
}
