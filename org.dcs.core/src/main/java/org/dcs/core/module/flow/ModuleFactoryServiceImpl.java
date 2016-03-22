package org.dcs.core.module.flow;

import java.util.List;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

import javax.enterprise.inject.Default;
import javax.enterprise.inject.spi.CDI;

import org.apache.nifi.components.PropertyDescriptor;
import org.apache.nifi.processor.ProcessContext;
import org.apache.nifi.processor.Relationship;
import org.dcs.api.service.RESTException;
import org.ops4j.pax.cdi.api.OsgiService;
import org.ops4j.pax.cdi.api.OsgiServiceProvider;
import org.ops4j.pax.cdi.api.Properties;
import org.ops4j.pax.cdi.api.Property;
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

	private ConcurrentHashMap<UUID, FlowModule> flowModuleMap = new ConcurrentHashMap<>();

	/* (non-Javadoc)
	 * @see org.dcs.core.module.flow.ModuleFactoryService#createFlowModule(java.lang.String)
	 */
	@Override
	public UUID createFlowModule(String className) {
		UUID randomUUID = UUID.randomUUID();

		try {
			Class<?> clazz = Class.forName(className);
			Object obj = CDI.current().select(clazz).get();
			if(obj instanceof FlowModule ) {
				flowModuleMap.put(randomUUID, (FlowModule)obj);
			} else {
				logger.warn("Given classname " + className + " is not of type FlowModule");
				return null;
			}
			return randomUUID;
		} catch (ClassNotFoundException e) {
			logger.warn("Error initialising class " + className);
			return null;
		}
	}

	/* (non-Javadoc)
	 * @see org.dcs.core.module.flow.ModuleFactoryService#getModule(java.util.UUID)
	 */
	@Override
	public FlowModule getModule(UUID moduleUUID) {
		return flowModuleMap.get(moduleUUID);
	}


	/* (non-Javadoc)
	 * @see org.dcs.core.module.flow.ModuleFactoryService#getPropertyDescriptors(java.util.UUID)
	 */
	@Override
	public List<PropertyDescriptor> getPropertyDescriptors(UUID moduleUUID) {
		return getModule(moduleUUID).getPropertyDescriptors();
	}

	/* (non-Javadoc)
	 * @see org.dcs.core.module.flow.ModuleFactoryService#getRelationships(java.util.UUID)
	 */
	@Override
	public List<Relationship> getRelationships(UUID moduleUUID) {
		return getModule(moduleUUID).getRelationships();
	}

	/* (non-Javadoc)
	 * @see org.dcs.core.module.flow.ModuleFactoryService#schedule(java.util.UUID)
	 */
	@Override
	public void schedule(UUID moduleUUID) {
		getModule(moduleUUID).schedule();
	}

	/* (non-Javadoc)
	 * @see org.dcs.core.module.flow.ModuleFactoryService#trigger(java.util.UUID, org.apache.nifi.processor.ProcessContext)
	 */
	@Override
	public Object trigger(UUID moduleUUID, final ProcessContext context) throws RESTException {		
		return getModule(moduleUUID).trigger(context);		
	}

	/* (non-Javadoc)
	 * @see org.dcs.core.module.flow.ModuleFactoryService#unschedule(java.util.UUID)
	 */
	@Override
	public void unschedule(UUID moduleUUID) {
		getModule(moduleUUID).unschedule();
	}

	/* (non-Javadoc)
	 * @see org.dcs.core.module.flow.ModuleFactoryService#stop(java.util.UUID)
	 */
	@Override
	public void stop(UUID moduleUUID) {
		getModule(moduleUUID).stop();
	}

	/* (non-Javadoc)
	 * @see org.dcs.core.module.flow.ModuleFactoryService#shutdown(java.util.UUID)
	 */
	@Override
	public void shutdown(UUID moduleUUID) {
		getModule(moduleUUID).shutdown();
	}

	/* (non-Javadoc)
	 * @see org.dcs.core.module.flow.ModuleFactoryService#remove(java.util.UUID)
	 */
	@Override
	public void remove(UUID moduleUUID) {
		try {
			getModule(moduleUUID).remove();
		} finally {
			flowModuleMap.remove(moduleUUID);
		}
	}
}
