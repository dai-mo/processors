package org.dcs.core.module.flow;

import java.util.List;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

import javax.enterprise.inject.spi.CDI;

import org.apache.nifi.components.PropertyDescriptor;
import org.apache.nifi.processor.ProcessContext;
import org.apache.nifi.processor.Relationship;
import org.dcs.api.service.RESTException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ModuleFactory {

	private static final Logger logger = LoggerFactory.getLogger(ModuleFactory.class);

	private ConcurrentHashMap<UUID, FlowModule> flowModuleMap = new ConcurrentHashMap<>();

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

	public FlowModule getModule(UUID moduleUUID) {
		return flowModuleMap.get(moduleUUID);
	}


	public List<PropertyDescriptor> getPropertyDescriptors(UUID moduleUUID) {
		return getModule(moduleUUID).getPropertyDescriptors();
	}

	public List<Relationship> getRelationships(UUID moduleUUID) {
		return getModule(moduleUUID).getRelationships();
	}

	public void schedule(UUID moduleUUID) {
		getModule(moduleUUID).schedule();
	}

	public Object trigger(UUID moduleUUID, final ProcessContext context) throws RESTException {		
		return getModule(moduleUUID).trigger(context);		
	}

	public void unschedule(UUID moduleUUID) {
		getModule(moduleUUID).unschedule();
	}

	public void stop(UUID moduleUUID) {
		getModule(moduleUUID).stop();
	}

	public void shutdown(UUID moduleUUID) {
		getModule(moduleUUID).shutdown();
	}

	public void remove(UUID moduleUUID) {
		try {
			getModule(moduleUUID).remove();
		} finally {
			flowModuleMap.remove(moduleUUID);
		}
	}
}
