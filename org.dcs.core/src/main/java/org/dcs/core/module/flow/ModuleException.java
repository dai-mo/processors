package org.dcs.core.module.flow;

public class ModuleException extends Exception {
	
	public ModuleException(String message) {
		super(message);
	}
	
	public ModuleException(String message, Throwable t) {
		super(message, t);
	}

}
