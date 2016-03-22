package org.dcs.core.module.flow;

import java.util.List;
import java.util.UUID;

import org.apache.nifi.components.PropertyDescriptor;
import org.apache.nifi.processor.ProcessContext;
import org.apache.nifi.processor.Relationship;
import org.dcs.api.service.RESTException;

public interface ModuleFactoryService {

	UUID createFlowModule(String className);

	FlowModule getModule(UUID moduleUUID);

	List<PropertyDescriptor> getPropertyDescriptors(UUID moduleUUID);

	List<Relationship> getRelationships(UUID moduleUUID);

	void schedule(UUID moduleUUID);

	Object trigger(UUID moduleUUID, ProcessContext context) throws RESTException;

	void unschedule(UUID moduleUUID);

	void stop(UUID moduleUUID);

	void shutdown(UUID moduleUUID);

	void remove(UUID moduleUUID);

}