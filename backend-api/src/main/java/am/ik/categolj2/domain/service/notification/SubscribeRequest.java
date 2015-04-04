package am.ik.categolj2.domain.service.notification;

import lombok.Data;
import org.hibernate.validator.constraints.NotEmpty;
import org.hibernate.validator.constraints.URL;

@Data
public class SubscribeRequest {
    @NotEmpty
    @URL
    private String endpoint;
    @NotEmpty
    private String subscriptionId;
}
