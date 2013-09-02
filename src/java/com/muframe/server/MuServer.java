package com.muframe.server;

import java.io.File;
import java.io.IOException;
import java.net.UnknownHostException;

import org.apache.commons.configuration.Configuration;
import org.apache.commons.configuration.EnvironmentConfiguration;
import org.apache.log4j.Logger;
import org.glassfish.grizzly.http.server.HttpServer;
import org.mockito.Mockito;

import com.mongodb.DB;
import com.mongodb.MongoClient;
import com.muframe.connectors.IMAPConnector;
import com.typesafe.config.Config;
import com.typesafe.config.ConfigFactory;

public class MuServer implements Runnable {
	private static final Logger logger = Logger.getLogger(MuServer.class);
	
	private static final Config config = ConfigFactory.load();

	
//	private static final String DB_SERVER_IP = config.getString("DB_SERVER_IP", "127.0.0.1");
//	private static final Integer DB_SERVER_PORT = config.getInteger("DB_SERVER_PORT", 27017);
	public static final String PHOTOS_FOLDER = config.getString("mu-server.photos-folder");
	
	private static final long SLEEPING_TIME = 60000; //milisecond


	private DB db;
	private ServerConnector connector;

	private static PhotosDisplay display;
	private final PhotoStore photoStore;
	
	//HACK SHIT FIXME can generate concurrency problems
	public static void showPhoto(String photo) {
		display.showPhoto(new File(PHOTOS_FOLDER+photo));
	}
	
	/**
	 * main run-loop, check apache-daemon for this
	 */
	public void run() {
		reloadLastPicture();
		
		for(;;) {
			PhotosHolder photos = null;
			if ( (photos = connector.retrievePhotos()) != null){
//				logger.debug("New photo: " + photo.getAbsolutePath());
				System.out.println("PEGOU FOTO NOVA");
				
				// the logic to alternate between photos has to come here?!
//				photoStore.addPhotoId(photo.getAbsolutePath());
//				display.showPhoto(photo);
			}
			try {
				Thread.sleep(SLEEPING_TIME);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}
	
	private void reloadLastPicture() {
		String lastPhoto = null;
		if ((lastPhoto = photoStore.getLastPhotoId()) != null) {
			File photo = new File(lastPhoto);
			
			if(photo.exists()) {
				display.showPhoto(photo);
			} else {
				logger.debug("Couldn't load last phooto. lastphoto: " + photoStore.getLastPhotoId());
			}
		}
	}
	
	private MuServer(DB db, ServerConnector connector, PhotosDisplay display, PhotoStore photoStore) {
		if (db == null || connector == null || photoStore == null) {
			throw new IllegalArgumentException("DB or Connector can not be null");
		}
		
		this.db = db;
		this.connector = connector;
		this.display = display;
		this.photoStore = photoStore;
	}
	
	public static Thread getInstance(DB db, ServerConnector connector, PhotosDisplay display, PhotoStore photoStore) {
		return new Thread(new MuServer(db, connector, display, photoStore));
	}
	
	public static void main(String[] args) throws IOException {
//		PIRSensor pir = PIRSensor.getInstance(display);

		PhotoStore.initializeStore();
//
		ServerConnector conn = IMAPConnector.getInstance(FileStorageService.getInstance(UUIDGenerator.getInstance(), PHOTOS_FOLDER));
		PhotosDisplay display = SwingPhotosDisplay.getInstance();
		PhotoStore photoStore = PhotoStore.getInstance();
		
		HttpServer httpServer = MuHttpServer.startServer();
//		System.in.read();
//		httpServer.stop();
		
		Thread server = MuServer.getInstance(Mockito.mock(DB.class), conn, display, photoStore);
		server.start();
		
//		System.out.println("turning on stuff");
	}
}
