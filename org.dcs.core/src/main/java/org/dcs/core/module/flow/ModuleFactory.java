package org.dcs.core.module.flow;

import java.util.UUID;

import javax.enterprise.inject.Any;
import javax.enterprise.inject.Instance;
import javax.enterprise.inject.spi.CDI;
import javax.inject.Inject;

import org.jboss.netty.util.internal.ConcurrentHashMap;

public class ModuleFactory {

	private ConcurrentHashMap<UUID, FlowModule> flowModuleMap = new ConcurrentHashMap<>();
	
	public UUID createFlowModule(String className) throws ModuleException {
		UUID randomUUID = UUID.randomUUID();

		try {
			Class<?> clazz = Class.forName(className);
			Object obj = CDI.current().select(clazz).get();
			if(obj instanceof FlowModule ) {
				flowModuleMap.put(randomUUID, (FlowModule)obj);
			} else {
				throw new ModuleException("Given classname is not of type FlowModule");
			}
			return randomUUID;
		} catch (ClassNotFoundException e) {
			throw new ModuleException("Error initialising class " + className, e);
		}
	}
	
	public FlowModule getModule(UUID moduleUUID) {
		return flowModuleMap.get(moduleUUID);
	}
	
	public FlowModule removeModule(UUID moduleUUID) {
		return flowModuleMap.remove(moduleUUID);
	}
}
