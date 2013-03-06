package com.muframe.server;

import java.io.InputStream;

/**
 * Storage Service for photos, text, etc.
 * The main goal of this interface is to abstract away from the particular implementations of storages available, 
 * such as files, databases, etc.
 * 
 * @author mariohct
 *
 */
public interface StorageService {
	
	/**
	 * Persists the data coming from a InputStream
	 * 
	 * @param in Data to be persisted
	 */
	public void persist(InputStream in);
}
