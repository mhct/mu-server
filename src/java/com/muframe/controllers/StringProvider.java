package com.muframe.controllers;

import javax.ws.rs.core.Context;
import javax.ws.rs.ext.Provider;

import com.sun.jersey.core.spi.component.ComponentContext;
import com.sun.jersey.core.spi.component.ComponentScope;
import com.sun.jersey.spi.inject.Injectable;
import com.sun.jersey.spi.inject.InjectableProvider;

@Provider
//FIXME not working
public class StringProvider implements InjectableProvider<Context, String> {

	@Override
	public ComponentScope getScope() {
		return ComponentScope.PerRequest;
	}

	@Override
	public Injectable<String> getInjectable(ComponentContext ic, Context a, String c) {
		return new Porra();
	}

}

class Porra implements Injectable<String> {

	@Override
	public String getValue() {
		return "MARIO";
	}
	
	
}
