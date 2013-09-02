package com.muframe.server;


/**
 * Connect to services which provide the photos to be used by the mu-server
 * 
 * @author mariohct
 *
 */
public interface ServerConnector {

	PhotosHolder retrievePhotos();
}
