package org.dcs.web.osgi;

import org.dcs.api.service.ModulesApiService;
import org.osgi.framework.BundleActivator;
import org.osgi.framework.BundleContext;
import org.osgi.framework.ServiceReference;
import org.osgi.util.tracker.ServiceTracker;

/**
 * Created by cmathew on 16/01/16.
 */
public class HostActivator implements BundleActivator
{
  private BundleContext m_context = null;
  // The service tacker object.
  private ServiceTracker m_tracker = null;

  public void start(BundleContext context) throws Exception
  {
    m_context = context;
    final ServiceReference<?>[] allServiceReferences = context.getAllServiceReferences(null, null);
    m_tracker = new ServiceTracker(
            m_context,
            ModulesApiService.class.getName(),
            null);
    m_tracker.open();

  }

  public void stop(BundleContext context)
  {
    m_context = null;
    m_tracker.close();
  }

  public BundleContext getContext()
  {
    return m_context;
  }

  public Object getService() {
    return m_tracker.getService();
  }
}
