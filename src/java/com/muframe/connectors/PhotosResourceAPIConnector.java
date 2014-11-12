package com.muframe.connectors;

import java.io.IOException;
import java.net.UnknownHostException;
import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;

import org.apache.log4j.Logger;

import retrofit.RestAdapter;
import retrofit.client.Response;

import com.example.helloworld.core.Photo;
import com.muframe.server.MuServer;
import com.muframe.server.PhotosHolder;
import com.muframe.server.ServerConnector;
import com.typesafe.config.Config;
import com.typesafe.config.ConfigFactory;

/**
 * Connects to SMTP server an retrieve photos
 * 
 * @author mariohct
 *
 */
public class PhotosResourceAPIConnector implements ServerConnector {
	private static final Logger logger = Logger.getLogger(PhotosResourceAPIConnector.class);
	
	private static final Config config = ConfigFactory.load();

	//FIXME remove this config from here.. the values below should come via the constructor. because in the future, 
	// we will want to change the username, ... at runtime. via a config screen
	private final String PHOTOS_SERVER = config.getString("mu-server.photosresource-connector.server");
	private final String USERNAME = config.getString("mu-server.photosresource-connector.username");
//	private final String PASSWORD = config.getString("mu-server.photosresource-connector.password");

	private final RestAdapter adapter;

	private final PhotosResourceAPI service;

	private Photo lastPhoto;
	

	private PhotosResourceAPIConnector() {
		adapter = new RestAdapter.Builder().setEndpoint(PHOTOS_SERVER).build();
		service = adapter.create(PhotosResourceAPI.class);
	}
	
	@Override
	public PhotosHolder retrievePhotos() {
		logger.info("Retrieving photos from PhotosResource server");
		logger.info("Photos server: " + PHOTOS_SERVER + "\tusername: " + USERNAME);
		
		PhotosHolder photos = PhotosHolder.getInstance();
		
	    try {
			Photo photo = service.getLatestPhotoId("mario");
			
			if (photo != null && !photo.equals(lastPhoto)) {
				logger.trace("received new photo" + photo.toString());
				
				lastPhoto = photo;
				String filename = photo.getUrl().toString().substring(photo.getUrl().toString().lastIndexOf("/"));
				Response res = service.getPhotoContent(USERNAME, filename);
	
				Path path = FileSystems.getDefault().getPath(MuServer.PHOTOS_FOLDER, filename);
				Files.copy(res.getBody().in(), path, StandardCopyOption.REPLACE_EXISTING);
	
				photos.add(path.toFile());
			}
			
	    } catch (UnknownHostException e) { 
	    	logger.error("Could not connect to cloud service: " + PHOTOS_SERVER);
		} catch (IOException e) {
			logger.error("Could not persist photo");
		}
	    
	    return photos;
	}
	
	public static PhotosResourceAPIConnector getInstance() {
		return new PhotosResourceAPIConnector();
	}
}
