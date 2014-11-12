package com.muframe.server;

import java.io.File;
import java.io.IOException;

import javax.swing.SwingUtilities;

import org.apache.log4j.Logger;

import com.muframe.connectors.PhotosResourceAPIConnector;
import com.typesafe.config.Config;
import com.typesafe.config.ConfigFactory;

import components.SimpleDisplay;

public class MuServer implements Runnable {
	private static final Logger logger = Logger.getLogger(MuServer.class);

	private static final Config config = ConfigFactory.load();
	public static final String RAW_PHOTOS_FOLDER = config.getString("mu-server.raw-photos-folder");
	public static final String PHOTOS_FOLDER = config.getString("mu-server.photos-folder");
	public static final String THUMBNAILS_FOLDER = config.getString("mu-server.thumbnails-folder");
	
	private static final long SLEEPING_TIME = config.getLong("mu-server.sleeping-time");



	private ServerConnector connector;
	private final PhotoStore photoStore;


	private static MuServer muServer;
	
	public static void showPhoto(final String photo) {
		SwingUtilities.invokeLater(new Runnable() {
			@Override
			public void run() {
				SimpleDisplay.changePhoto(new File(PHOTOS_FOLDER + "/" + photo));
			}
		});
	}
	
	/**
	 * main run-loop, check apache-daemon for this
	 */
	public void run() {
		reloadLastPicture();
		
		for(;;) {
			try {
				PhotosHolder photos = null;
				if ( (photos = connector.retrievePhotos()) != null){ 
					logger.debug("Number of photos: " + photos.size());
					
					for (File photo: photos) {
						photoStore.addPhotoId(PHOTOS_FOLDER + "/" + photo.getName());
					}
					// the logic to alternate between photos has to come here?!
					if (photos.size() != 0) {
						String lastPhotoId = photoStore.getLastPhotoId();
						logger.debug("LastPhotoId: " + lastPhotoId);
						
						if (lastPhotoId != null) {
							final File photo = new File(lastPhotoId);
							if (photo != null) {
								SwingUtilities.invokeLater(new Runnable() {
									@Override
									public void run() {
										SimpleDisplay.changePhoto(photo);
									}
								});
							}
						}
					}
				}
				Thread.sleep(SLEEPING_TIME);
			} catch (InterruptedException e) {
				logger.error("Thread interrupted", e);
			}
		}
	}
	
	private void reloadLastPicture() {
		String lastPhoto = null;
		if ((lastPhoto = photoStore.getLastPhotoId()) != null) {
			final File photo = new File(lastPhoto);
			
			if(photo.exists()) {
				SwingUtilities.invokeLater(new Runnable() {
					@Override
					public void run() {
						SimpleDisplay.changePhoto(photo);
					}
				});
			} else {
				logger.debug("Couldn't load last phooto. lastphoto: " + photoStore.getLastPhotoId());
			}
		}
	}
	
	private MuServer(ServerConnector connector, PhotosDisplay display, PhotoStore photoStore) {
		if (connector == null || photoStore == null) {
			throw new IllegalArgumentException("DB or Connector can not be null");
		}
		
		this.connector = connector;
		this.photoStore = photoStore;
	}
	
	public static Thread getInstance(ServerConnector connector, PhotosDisplay display, PhotoStore photoStore) {
		MuServer.muServer = new MuServer(connector, display, photoStore);
		return new Thread(MuServer.muServer);
	}
	
	public static void main(String[] args) throws IOException, InterruptedException {
//		PIRSensor pir = PIRSensor.getInstance(display);

		PhotoStore.initializeStore();
//
		ServerConnector conn = PhotosResourceAPIConnector.getInstance();
		SimpleDisplay.createGUI();
		PhotoStore photoStore = PhotoStore.getInstance();
		
		//TEST
//		HttpServer httpServer = MuHttpServer.startServer();
		
//		System.in.read();
//		httpServer.stop();
		
		Thread server = MuServer.getInstance(conn, null, photoStore);
		server.start();
	}
}
