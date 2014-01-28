package am.ik.categolj2.api.user;

import am.ik.categolj2.domain.validation.UserEmail;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.NotEmpty;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserResource {
    private String id;
    @NotNull
    @Size(min = 1, max = 128)
    private String username;
    @NotNull
    @Size(min = 1, max = 256)
    private String password;

    @UserEmail
    private String email;

    private boolean enabled;

    private boolean locked;

    private String firstName;

    private String lastName;

    @NotEmpty
    private List<Integer> roles;
}
