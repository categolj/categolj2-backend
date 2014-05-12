package am.ik.categolj2.api.error;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import lombok.Getter;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

@Getter
public class ApiError implements Serializable {

    private static final long serialVersionUID = 1L;
    private final String code;
    private final String message;

    @JsonSerialize(include = JsonSerialize.Inclusion.NON_EMPTY)
    private final String target;

    @JsonSerialize(include = JsonSerialize.Inclusion.NON_EMPTY)
    private final List<ApiError> details = new ArrayList<>();

    public ApiError(String code, String message) {
        this(code, message, null);
    }

    public ApiError(String code, String message, String target) {
        this.code = code;
        this.message = message;
        this.target = target;
    }

    public void addDetail(ApiError detail) {
        details.add(detail);
    }

}