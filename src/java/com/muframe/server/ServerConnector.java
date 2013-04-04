package com.muframe.server;

import java.io.File;

/**
 * Connect to services which provide the photos to be used by the mu-server
 * 
 * @author mariohct
 *
 */
public interface ServerConnector {

	File retrievePhotos();
}
