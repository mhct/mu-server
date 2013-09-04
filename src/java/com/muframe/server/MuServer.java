package com.muframe.server;

import java.io.File;
import java.io.IOException;

import org.apache.log4j.Logger;
import org.glassfish.grizzly.http.server.HttpServer;
import org.im4java.core.ConvertCmd;
import org.im4java.core.IM4JavaException;
import org.im4java.core.IMOperation;

import com.muframe.connectors.IMAPConnector;
import com.typesafe.config.Config;
import com.typesafe.config.ConfigFactory;

public class MuServer implements Runnable {
	private static final Logger logger = Logger.getLogger(MuServer.class);

	private static final Config config = ConfigFactory.load();
	public static final String PHOTOS_FOLDER = config.getString("mu-server.photos-folder");
	private static final long SLEEPING_TIME = 60000; //milisecond


	private ServerConnector connector;
	private final PhotosDisplay display;
	private final PhotoStore photoStore;
	private static MuServer muServer;
	
	//HACK SHIT FIXME can generate concurrency problems
	public static void showPhoto(String photo) {
		MuServer.muServer.display.showPhoto(new File(PHOTOS_FOLDER+photo));
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
				System.out.println("Has New photos");
				
				for (File photo: photos) {
					photoStore.addPhotoId(photo.getAbsolutePath());
//					convertPhoto(photo.getAbsolutePath());
				}
				// the logic to alternate between photos has to come here?!
				
				display.showPhoto(new File(photoStore.getLastPhotoId()));
			}
			try {
				Thread.sleep(SLEEPING_TIME);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}
	
	private void convertPhoto(String original, String converted) {
		ConvertCmd cmd = new ConvertCmd();
		IMOperation op = new IMOperation();
		op.resize(1920, 1080);
		op.colorspace("RGB");
		op.addImage(original, converted);
		try {
			cmd.run(op, original, converted);
		} catch (IOException | InterruptedException | IM4JavaException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
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
	
	private MuServer(ServerConnector connector, PhotosDisplay display, PhotoStore photoStore) {
		if (connector == null || display == null || photoStore == null) {
			throw new IllegalArgumentException("DB or Connector can not be null");
		}
		
		this.connector = connector;
		this.display = display;
		this.photoStore = photoStore;
	}
	
	public static Thread getInstance(ServerConnector connector, PhotosDisplay display, PhotoStore photoStore) {
		MuServer.muServer = new MuServer(connector, display, photoStore);
		return new Thread(MuServer.muServer);
	}
	
	public static void main(String[] args) throws IOException {
//		PIRSensor pir = PIRSensor.getInstance(display);

		PhotoStore.initializeStore();
//
		ServerConnector conn = IMAPConnector.getInstance();
		PhotosDisplay display = SwingPhotosDisplay.getInstance();
		PhotoStore photoStore = PhotoStore.getInstance();
		
		HttpServer httpServer = MuHttpServer.startServer();
//		System.in.read();
//		httpServer.stop();
		
		Thread server = MuServer.getInstance(conn, display, photoStore);
		server.start();
		
//		System.out.println("turning on stuff");
	}
}
