package am.ik.categolj2.domain.service.notification;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Collection;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class NotificationRequest {
    @JsonProperty("registration_ids")
    private Collection<String> registration_ids;
}
