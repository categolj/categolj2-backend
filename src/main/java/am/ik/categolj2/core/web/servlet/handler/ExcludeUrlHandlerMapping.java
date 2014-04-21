package am.ik.categolj2.core.web.servlet.handler;

import lombok.Setter;
import org.springframework.web.servlet.handler.SimpleUrlHandlerMapping;

import javax.servlet.http.HttpServletRequest;
import java.util.Set;


public class ExcludeUrlHandlerMapping extends SimpleUrlHandlerMapping {
    @Setter
    private Set<String> excludes;

    @Override
    protected Object lookupHandler(String urlPath, HttpServletRequest request) throws Exception {
        if (excludes.contains(urlPath)) {
            return null;
        }
        return super.lookupHandler(urlPath, request);
    }
}
