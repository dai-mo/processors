package org.dcs.servlet.basic;

import org.osgi.framework.BundleActivator;
import org.osgi.framework.BundleContext;
import org.osgi.util.tracker.ServiceTracker;

/**
 * Created by cmathew on 28/12/15.
 */
public class Activator implements BundleActivator {

    private static BundleContext context;

    private ServiceTracker httpServiceTracker;

    static BundleContext getContext() {
        return context;
    }

    public void start(BundleContext bundleContext) throws Exception {
        Activator.context = bundleContext;
        httpServiceTracker = new HttpServiceTracker(context);
        httpServiceTracker.open();
    }

    public void stop(BundleContext bundleContext) throws Exception {
        Activator.context = null;
        httpServiceTracker.close();
    }

}
