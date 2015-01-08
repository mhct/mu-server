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

import com.muframe.dao.MuServerConfiguration;
import com.muframe.dao.Photo;
import com.muframe.server.MuServer;
import com.muframe.server.PhotosHolder;
import com.muframe.server.ServerConnector;

/**
 * Connects to cloud REST server to retrieve photos
 * 
 * @author mariohct
 *
 */
public class PhotosResourceAPIConnector implements ServerConnector {
	private static final Logger logger = Logger.getLogger(PhotosResourceAPIConnector.class);
	
	private final MuServerConfiguration config;

	private final RestAdapter adapter;

	private final PhotosResourceAPI service;

	private Photo lastPhoto;
	

	private PhotosResourceAPIConnector(final MuServerConfiguration config) {
		this.config = config;
		adapter = new RestAdapter.Builder().setEndpoint(config.getPhotosServer()).build();
		service = adapter.create(PhotosResourceAPI.class);
	}
	
	@Override
	public PhotosHolder retrievePhotos() {
		logger.info("Retrieving photos from PhotosResource server");
		logger.info("Photos server: " + config.getPhotosServer() + "\tusername: " + config.getUsername());
		
		PhotosHolder photos = PhotosHolder.getInstance();
		
	    try {
			Photo photo = service.getLatestPhotoId(config.getUsername());
			
			if (photo != null && !photo.equals(lastPhoto)) {
				logger.trace("received new photo" + photo.toString());
				
				lastPhoto = photo;
				String filename = photo.getUrl().toString().substring(photo.getUrl().toString().lastIndexOf("/"));
				Response res = service.getPhotoContent(config.getUsername(), filename);
	
				Path path = FileSystems.getDefault().getPath(MuServer.PHOTOS_FOLDER, filename);
				Files.copy(res.getBody().in(), path, StandardCopyOption.REPLACE_EXISTING);
	
				photos.add(path.toFile());
			}
			
	    } catch (UnknownHostException e) { 
	    	logger.error("Could not connect to cloud service: " + config.getPhotosServer());
		} catch (IOException e) {
			logger.error("Could not persist photo");
		} catch (Exception e) {
			logger.error("Could not persist photo", e);
		}
	    
	    return photos;
	}
	
	public static PhotosResourceAPIConnector getInstance(MuServerConfiguration config) {
		return new PhotosResourceAPIConnector(config);
	}
}
