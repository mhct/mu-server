package com.muframe.connectors;

import java.util.List;

import com.muframe.dao.Photo;

import retrofit.client.Response;
import retrofit.http.GET;
import retrofit.http.Path;
import retrofit.http.Streaming;


/**
 * API to access the PhotoResource from missyouframe
 * This API is the same exposed by the MU-Cloud (DropWizardTest) com.example.hellowworld.resources.PhotosResource
 * 
 * 12/11/2014
 * 
 * @author mario
 *
 */
public interface PhotosResourceAPI {
	
	@GET("/{user}/photos")
	public List<Photo> getPhotos(@Path("user") String user);
	
	@GET("/{user}/photos/latest")
	public Photo getLatestPhotoId(@Path("user") String user);
	
	@GET("/{user}/photos/{id}")
	public Photo getPhoto(@Path("user") String user,
			@Path("id") String id);
	
	@GET("/{user}/photos/processed/{filename}")
	@Streaming
	public Response getPhotoContent(@Path("user") String user,
			@Path("filename") String filename);
			
}
