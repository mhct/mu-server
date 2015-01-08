package com.muframe.connectors;

import retrofit.http.GET;
import retrofit.http.Path;

import com.muframe.dao.MuServerConfiguration;


/**
 * API to access the PhotoResource from missyouframe
 * This API is the same exposed by the MU-Cloud (DropWizardTest) com.example.hellowworld.resources.PhotosResource
 * 
 * 12/11/2014
 * 
 * @author mario
 *
 */
public interface ConfigurationResourceAPI {
	
	@GET("/{user}/configuration")
	public MuServerConfiguration getConfiguration(@Path("user") String user);
}
