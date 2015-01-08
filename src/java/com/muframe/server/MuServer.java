package com.muframe.server;

import java.io.File;
import java.io.IOException;

import javax.swing.SwingUtilities;

import org.apache.log4j.Logger;
import org.joda.time.DateTime;
import org.joda.time.Duration;
import org.joda.time.Period;

import retrofit.RestAdapter;

import com.muframe.connectors.ConfigurationResourceAPI;
import com.muframe.connectors.PhotosResourceAPIConnector;
import com.muframe.dao.MuServerConfiguration;
import com.typesafe.config.Config;
import com.typesafe.config.ConfigFactory;
import components.SimpleDisplay;

public class MuServer implements Runnable {
	private static final Logger logger = Logger.getLogger(MuServer.class);

	private static final Config config = ConfigFactory.load();
	private static final long SLEEPING_TIME = config.getLong("mu-server.sleeping-time");
	public static final String RAW_PHOTOS_FOLDER = config.getString("mu-server.raw-photos-folder");
	public static final String PHOTOS_FOLDER = config.getString("mu-server.photos-folder");
	public static final String THUMBNAILS_FOLDER = config.getString("mu-server.thumbnails-folder");

	private MuServerConfiguration currentConfiguration;
	private ServerConnector connector;
	private ConfigurationResourceAPI configurationService;

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
			currentConfiguration = configurationService.getConfiguration(currentConfiguration.getUsername());
			System.out.println("PhotoChangeInterval: " + currentConfiguration.getPhotoChangeInterval());
			try {
				PhotosHolder photos = null;
				if ( (photos = connector.retrievePhotos()) != null && !photos.empty()){ 
					logger.debug("Number of photos: " + photos.size());
					
					for (File photo: photos) {
						photoStore.addPhotoId(PHOTOS_FOLDER + "/" + photo.getName());
					}
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
				else if (new Period(new Duration(SimpleDisplay.getTimeLastChange(), DateTime.now())).getMinutes() >= currentConfiguration.getPhotoChangeInterval()){
					// no new photos, show random old photo
					final File photo = new File(photoStore.getRandomPhotoId());
					if (photo != null) {
						SwingUtilities.invokeLater(new Runnable() {
							@Override
							public void run() {
								SimpleDisplay.changePhoto(photo);
							}
						});
					}
				}
				Thread.sleep(SLEEPING_TIME);
			} catch (InterruptedException e) {
				logger.error("Thread interrupted", e);
			} catch (Exception e) {
				logger.error("MuServer Thread interrupted", e);
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
	
	private MuServer(ServerConnector connector, PhotosDisplay display, PhotoStore photoStore, ConfigurationResourceAPI configurationService, MuServerConfiguration configuration) {
		if (connector == null || photoStore == null) {
			throw new IllegalArgumentException("DB or Connector can not be null");
		}
		
		this.connector = connector;
		this.photoStore = photoStore;
		this.configurationService = configurationService;
		this.currentConfiguration = configuration;
	}
	
	public static Thread getInstance(ServerConnector connector, PhotosDisplay display, PhotoStore photoStore, ConfigurationResourceAPI configurationService, MuServerConfiguration configuration) {
		MuServer.muServer = new MuServer(connector, display, photoStore, configurationService, configuration);
		return new Thread(MuServer.muServer);
	}
	
	public static void main(String[] args) throws IOException, InterruptedException {
//		PIRSensor pir = PIRSensor.getInstance(display);

		PhotoStore.initializeStore();
		SimpleDisplay.createGUI();

		// Service to retrieve configuration from the Cloud
		final String photosServer = config.getString("mu-server.photosresource-connector.server");
		final String username = config.getString("mu-server.photosresource-connector.username");
		
		System.out.println(photosServer);
		final RestAdapter adapter = new RestAdapter.Builder().setEndpoint(photosServer).build();
		final ConfigurationResourceAPI configurationService = adapter.create(ConfigurationResourceAPI.class);
		MuServerConfiguration configuration = configurationService.getConfiguration(username);
		
		ServerConnector conn = PhotosResourceAPIConnector.getInstance(configuration);
		PhotoStore photoStore = PhotoStore.getInstance();
		
		
		//TEST
//		HttpServer httpServer = MuHttpServer.startServer();
		
//		System.in.read();
//		httpServer.stop();
		
		Thread server = MuServer.getInstance(conn, null, photoStore, configurationService, configuration);
		server.start();
	}
}
