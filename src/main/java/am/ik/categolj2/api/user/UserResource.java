package am.ik.categolj2.api.user;

import am.ik.categolj2.domain.validation.UserEmail;
import am.ik.categolj2.domain.validation.Username;
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
    public static interface Create {
    }

    public static interface Update {
    }

    private String id;
    @NotNull
    @Username
    private String username;
    @NotNull(groups = Create.class)
    @Size(min = 1, max = 256)
    private String password;
    @NotNull
    @UserEmail
    private String email;

    private boolean enabled;

    private boolean locked;
    @NotNull
    @Size(min = 1, max = 128)
    private String firstName;
    @NotNull
    @Size(min = 1, max = 128)
    private String lastName;

    @NotEmpty
    private List<Integer> roles;
}
