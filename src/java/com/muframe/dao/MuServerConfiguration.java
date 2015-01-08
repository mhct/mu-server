package com.muframe.dao;

import javax.persistence.Id;

import com.fasterxml.jackson.annotation.JsonProperty;


/**
 * Configuration metadata for the MuServer
 * 
 * @author mario
 *
 */
public class MuServerConfiguration {
	@Id
	private String id;

	private String photosServer;
	private String username;
	private int photoChangeInterval;
	
	public MuServerConfiguration() {}
	
	public MuServerConfiguration(String id, String photosServer, String username, int photoChangeInterval) {
		this.id = id;
		this.photosServer = photosServer;
		this.username = username;
		this.photoChangeInterval = photoChangeInterval;
	}
	
	@JsonProperty
	public String getId() {
		return id;
	}
	
	@JsonProperty
	public int getPhotoChangeInterval() {
		return photoChangeInterval;
	}

	@JsonProperty
	public String getPhotosServer() {
		return photosServer;
	}

	@JsonProperty
	public String getUsername() {
		return username;
	}
}
