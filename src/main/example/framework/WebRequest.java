package example.framework;

import org.apache.commons.lang.StringUtils;

import javax.servlet.http.Cookie;
import java.util.List;

public class WebRequest implements Request {

    private final RequestContext request;
    private final PathVariables pathVariables;
    private final IdentityFactory identityFactory;

    public WebRequest(RequestContext request, IdentityFactory identityFactory, PathVariables pathVariables) {
        this.identityFactory = identityFactory;
        this.pathVariables = pathVariables;
        this.request = request;
    }

    public String getParameter(String name) {
        return request.getParameter(name);
    }

    public List<String> getParameters(String name) {
        return request.getParameterValues(name);
    }

    public String getPathVariable(String name) {
        return StringUtils.defaultString(pathVariables.get(name));
    }

    public Identity getIdentity(String name) {
        return identityFactory.createFrom(getPathVariable(name));
    }

    public Cookie getCookie(String name) {
        for (Cookie cookie : request.getCookies()) {
            if (cookie.getName().equals(name)) {
                return cookie;
            }
        }
        return null;
    }

    public String getCookieValue(String name) {
        Cookie cookie = getCookie(name);
        return (cookie != null) ? cookie.getValue() : "";
    }
}
