package example.framework;

import example.utils.Pair;

import java.util.List;

public class WebApplication implements Application {

    private final PicoContainer applicationScope = new PicoContainer();
    private final RouteFinder routeFinder = new RouteFinder();

    private List<Component> components;

    public WebApplication(List<Component> components, Object... instances) {
        this.components = components;
        for (Object instance : instances) {
            applicationScope.registerInstance(instance);
        }
        for (Component component : components) {
            component.registerApplicationScope(applicationScope);
            component.registerRoutes(routeFinder);
        }
    }

    public Response process(RequestContext request) {
        RequestMethod method = request.getMethod();
        String lookupPath = request.getLookupPath();
        PicoContainer requestScope = createRequestScope(method);
        try {
            Pair<Route, PathVariables> mapping = routeFinder.findRoute(method, lookupPath, requestScope);
            PathVariables pathVars = mapping.getValue();
            Route route = mapping.getKey();

            IdentityFactory identityFactory = requestScope.get(IdentityFactory.class);
            return route.process(new WebRequest(request, identityFactory, pathVars));

        } catch (Exception e) {
            ErrorHandler handler = requestScope.get(ErrorHandler.class);
            return handler.handleError(method, lookupPath, e);

        } finally {
            requestScope.dispose();
        }
    }

    private PicoContainer createRequestScope(Object... instances) {
        PicoContainer requestScope = new PicoContainer(applicationScope);
        for (Object instance : instances) {
            requestScope.registerInstance(instance);
        }
        for (Component component : components) {
            component.registerRequestScope(requestScope);
        }
        return requestScope;
    }

    public void dispose() {
        applicationScope.dispose();
    }
}
