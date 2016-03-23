package org.dcs.core.module.flow;

import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

import javax.enterprise.inject.Default;
import javax.inject.Inject;

import org.dcs.api.service.ModuleFactoryService;
import org.dcs.api.service.RESTException;
import org.ops4j.pax.cdi.api.OsgiServiceProvider;
import org.ops4j.pax.cdi.api.Properties;
import org.ops4j.pax.cdi.api.Property;
import org.osgi.framework.BundleContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@OsgiServiceProvider
@Properties({
    @Property(name = "service.exported.interfaces", value = "*"),
    @Property(name = "service.exported.configs", value = "org.apache.cxf.ws")
})
@Default
public class ModuleFactoryServiceImpl implements ModuleFactoryService {

	private static final Logger logger = LoggerFactory.getLogger(ModuleFactoryServiceImpl.class);

	private ConcurrentHashMap<String, FlowModule> flowModuleMap = new ConcurrentHashMap<>();

	@Inject
	private BundleContext bundleContext;

	/* (non-Javadoc)
	 * @see org.dcs.core.module.flow.ModuleFactoryService#createFlowModule(java.lang.String)
	 */
	@Override
	public String createFlowModule(String className) {
		String randomUUID = UUID.randomUUID().toString();

		try {
			Class<?> clazz = Class.forName(className);			 			
			Object obj = clazz.newInstance();

			if(obj instanceof FlowModule ) {
				FlowModule fm = (FlowModule)obj;
				fm.init(bundleContext);
				flowModuleMap.put(randomUUID, fm);
			} else {
				logger.warn("Given classname " + className + " is not of type FlowModule");
				return null;
			}
			return randomUUID;
		} catch (ClassNotFoundException | InstantiationException | IllegalAccessException e) {
			logger.warn("Error initialising class " + className);
			return null;
		}
	}


	public FlowModule getModule(String moduleUUID) {
		return flowModuleMap.get(moduleUUID);
	}


	/* (non-Javadoc)
	 * @see org.dcs.core.module.flow.ModuleFactoryService#getPropertyDescriptors(java.util.UUID)
	 */
	@Override
	public Map<String, java.util.Properties> getPropertyDescriptors(String moduleUUID) {
		return getModule(moduleUUID).getPropertyDescriptors();
	}

	/* (non-Javadoc)
	 * @see org.dcs.core.module.flow.ModuleFactoryService#getRelationships(java.util.UUID)
	 */
	@Override
	public Map<String, java.util.Properties> getRelationships(String moduleUUID) {
		return getModule(moduleUUID).getRelationships();
	}

	/* (non-Javadoc)
	 * @see org.dcs.core.module.flow.ModuleFactoryService#schedule(java.util.UUID)
	 */
	@Override
	public boolean schedule(String moduleUUID) {
		getModule(moduleUUID).schedule();
		return true;
	}

	/* (non-Javadoc)
	 * @see org.dcs.core.module.flow.ModuleFactoryService#trigger(java.util.UUID, org.apache.nifi.processor.ProcessContext)
	 */
	@Override
	public Object trigger(String moduleUUID,  Map<String, java.util.Properties> properties) throws RESTException {		
		return getModule(moduleUUID).trigger(properties);		
	}

	/* (non-Javadoc)
	 * @see org.dcs.core.module.flow.ModuleFactoryService#unschedule(java.util.UUID)
	 */
	@Override
	public boolean unschedule(String moduleUUID) {
		getModule(moduleUUID).unschedule();
		return true;
	}

	/* (non-Javadoc)
	 * @see org.dcs.core.module.flow.ModuleFactoryService#stop(java.util.UUID)
	 */
	@Override
	public boolean stop(String moduleUUID) {
		getModule(moduleUUID).stop();
		return true;
	}

	/* (non-Javadoc)
	 * @see org.dcs.core.module.flow.ModuleFactoryService#shutdown(java.util.UUID)
	 */
	@Override
	public boolean shutdown(String moduleUUID) {
		getModule(moduleUUID).shutdown();
		return true;
	}

	/* (non-Javadoc)
	 * @see org.dcs.core.module.flow.ModuleFactoryService#remove(java.util.UUID)
	 */
	@Override
	public boolean remove(String moduleUUID) {
		try {
			getModule(moduleUUID).remove();
		} finally {
			flowModuleMap.remove(moduleUUID);
		}
		return true;
	}
}
