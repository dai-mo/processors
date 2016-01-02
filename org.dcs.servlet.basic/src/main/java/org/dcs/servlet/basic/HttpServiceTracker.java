package org.dcs.servlet.basic;

import org.osgi.framework.BundleContext;
import org.osgi.framework.ServiceReference;
import org.osgi.service.http.HttpService;
import org.osgi.service.http.NamespaceException;
import org.osgi.util.tracker.ServiceTracker;

import javax.servlet.ServletException;

/**
 * Created by cmathew on 28/12/15.
 */
public class HttpServiceTracker extends ServiceTracker {

    private static final String SERVLET_ALIAS = "/api/basic";
    private BundleContext context;

    public HttpServiceTracker(BundleContext context) {
        super(context, HttpService.class.getName(), null);
        this.context = context;
    }

    public Object addingService(ServiceReference reference) {
        HttpService httpService = (HttpService) context.getService(reference);
        System.out.println("Adding HTTP Service");
        try {
            httpService.registerServlet(SERVLET_ALIAS, new TestServlet(), null, null);
        } catch (ServletException | NamespaceException e) {
            System.err.println("Servlet couldn't be registered: " + e.getMessage());
        }
        return httpService;
    }

    public void removedService(ServiceReference reference, Object service) {
        super.removedService(reference, service);
    }
}
