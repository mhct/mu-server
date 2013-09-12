package com.muframe.server;

import java.io.File;
import java.io.IOException;

import javax.swing.SwingUtilities;

import org.apache.log4j.Logger;
import org.im4java.core.ConvertCmd;
import org.im4java.core.IM4JavaException;
import org.im4java.core.IMOperation;

import com.muframe.connectors.IMAPConnector;
import com.typesafe.config.Config;
import com.typesafe.config.ConfigFactory;
import components.SimpleDisplay;

public class MuServer implements Runnable {
	private static final Logger logger = Logger.getLogger(MuServer.class);

	private static final Config config = ConfigFactory.load();
	public static final String RAW_PHOTOS_FOLDER = config.getString("mu-server.raw-photos-folder");
	public static final String PHOTOS_FOLDER = config.getString("mu-server.photos-folder");
	private static final long SLEEPING_TIME = 30000; //milisecond


	private ServerConnector connector;
//	private final PhotosDisplay display;
	private final PhotoStore photoStore;
	private static MuServer muServer;
	
	//HACK SHIT FIXME can generate concurrency problems
	public static void showPhoto(final String photo) {
//		MuServer.muServer.display.showPhoto(new File(PHOTOS_FOLDER + "/" +photo));
		
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
//		reloadLastPicture();
		
		for(;;) {
			PhotosHolder photos = null;
			if ( (photos = connector.retrievePhotos()) != null){
				logger.debug("Has New photos");
				logger.debug("Number of photos: " + photos.size());
				
				//TODO refactor this out
				for (File photo: photos) {
					convertPhoto(photo.getAbsolutePath(), PHOTOS_FOLDER + "/" + photo.getName());
					photoStore.addPhotoId(PHOTOS_FOLDER + "/" + photo.getName());
//					convertPhoto(photo.getAbsolutePath());
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
			try {
				Thread.sleep(SLEEPING_TIME);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}
	
	private void convertPhoto(String original, String converted) {
		logger.debug("Resizing photo: " + original + " to " + converted);
		ConvertCmd cmd = new ConvertCmd();
		IMOperation op = new IMOperation();
		op.resize(1920, 1080);
//		op.colorspace("RGB");
		op.addImage(original);
		op.addImage(converted);
		try {
			cmd.run(op);
		} catch (IOException | InterruptedException | IM4JavaException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			logger.debug("Resize error");
		}
		
		logger.debug("Resize finished");		
	}
	
//	private void reloadLastPicture() {
//		String lastPhoto = null;
//		if ((lastPhoto = photoStore.getLastPhotoId()) != null) {
//			File photo = new File(lastPhoto);
//			
//			if(photo.exists()) {
//				display.showPhoto(photo);
//			} else {
//				logger.debug("Couldn't load last phooto. lastphoto: " + photoStore.getLastPhotoId());
//			}
//		}
//	}
	
	private MuServer(ServerConnector connector, PhotosDisplay display, PhotoStore photoStore) {
//		if (connector == null || display == null || photoStore == null) {
		if (connector == null || photoStore == null) {
			throw new IllegalArgumentException("DB or Connector can not be null");
		}
		
		this.connector = connector;
//		this.display = display;
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
		ServerConnector conn = IMAPConnector.getInstance();
//		PhotosDisplay display = SwingPhotosDisplay.getInstance();
		SimpleDisplay.createGUI();
		PhotoStore photoStore = PhotoStore.getInstance();
		
		//TEST
//		HttpServer httpServer = MuHttpServer.startServer();
		
//		System.in.read();
//		httpServer.stop();
		
		Thread server = MuServer.getInstance(conn, null, photoStore);
		server.start();
		
//		System.out.println("turning on stuff");
	}
}
