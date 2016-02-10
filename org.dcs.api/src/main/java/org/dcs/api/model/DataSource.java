package org.dcs.api.model;

import java.util.UUID;

import javax.validation.constraints.NotNull;

public class DataSource {
	
	private final UUID uuid;
	private final String name;
	private final String url;
	
	public DataSource(@NotNull UUID uuid, @NotNull String name, @NotNull String url) {
		this.uuid = uuid;
		this.name = name;
		this.url = url;		
	}

	public UUID getUuid() {
		return uuid;
	}

	public String getName() {
		return name;
	}

	public String getUrl() {
		return url;
	}

	@Override
	public boolean equals(Object obj) {
		if(obj instanceof DataSource) {
			DataSource that = (DataSource)obj;
			return this.uuid.equals(that.uuid) && this.name.equals(that.name) && this.url.equals(that.url);
		}
		return false;
	}
	
	@Override
	public int hashCode() {
		return uuid.hashCode() + name.hashCode() + url.hashCode();
	}
}
