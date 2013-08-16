package com.muframe.server;

import java.io.IOException;
import java.net.URI;
import java.util.Collections;
import java.util.Map;

import javax.ws.rs.core.UriBuilder;

import org.glassfish.grizzly.http.server.HttpServer;

import com.google.common.collect.Maps;
import com.sun.jersey.api.container.grizzly2.GrizzlyServerFactory;
import com.sun.jersey.api.core.PackagesResourceConfig;
import com.sun.jersey.api.core.ResourceConfig;


public class MuHttpServer {

	private static final URI BASE_URI = getBaseURI();
	private static final Map<String, Object> configurations = getConfigurations();
	
	private static URI getBaseURI() {
		return UriBuilder.fromUri("http://127.0.0.1/").port(8089).build();
	}
	
	private static Map<String, Object> getConfigurations() {
		Map<String, Object> res = Maps.newHashMap();
		res.put("com.sun.jersey.api.json.POJOMappingFeature", Boolean.TRUE);
		
		return Collections.unmodifiableMap(res);
	}
	
	public static HttpServer startServer() throws IOException {
		System.out.println("Starting grizzly");
		HttpServer server = null;
		try {
			ResourceConfig rc = new PackagesResourceConfig("com.muframe.controllers");
			rc.setPropertiesAndFeatures(configurations);
			server = GrizzlyServerFactory.createHttpServer(BASE_URI, rc);
		} catch (Exception e) {
			System.out.println(e.toString());
		}
		return server;
	}
}

