package org.dcs.api;

@javax.annotation.Generated(value = "class io.swagger.codegen.languages.JaxRSServerCodegen", date = "2016-01-15T18:35:39.348+01:00")
public class NotFoundException extends ApiException {
	private int code;
	public NotFoundException(int code, String msg) {
		super(code, msg);
		this.code = code;
	}
}
