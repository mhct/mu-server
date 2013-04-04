package com.muframe.server;

import java.io.File;
import java.io.IOException;

import org.apache.log4j.Logger;

public class PhotosDisplay implements Runnable {
	private static final Logger logger = Logger.getLogger(PhotosDisplay.class);
	
	private File newPhoto;
	private File currentPhoto;
	private final long  SLEEPING_TIME = 1000; //1 second

	private boolean showNewPhoto;

	private PhotosDisplay() {}
	
	public static PhotosDisplay getInstance() {
		PhotosDisplay display = new PhotosDisplay();
		Thread thread = new Thread(display);
		thread.start();
		
		return display;
	}
	
	@Override
	public void run() {
		for(;;) {
			if ( newPhoto != null && showNewPhoto){
				logger.debug("Received new photo do be displayed");
				try {
					killCurrentPhoto();
					showNewPhoto();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
			try {
				Thread.sleep(SLEEPING_TIME );
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}
	
	private void showNewPhoto() throws IOException {
		logger.debug("Loading new fbi process");
		currentPhoto = newPhoto;
		newPhoto = null;
		showNewPhoto = false;
		Runtime.getRuntime().exec("/usr/bin/fbi -T 1 -noverbose -m 1920x1080 -a " + currentPhoto.getAbsolutePath() + " &");
	}

	private void killCurrentPhoto() throws IOException {
		logger.debug("Killing current FBI process");
		Runtime.getRuntime().exec("/usr/bin/killall fbi");
	}

	public void showPhoto(File photo) {
		synchronized(this){
			newPhoto = photo;
			showNewPhoto = true;
		}
	}
	
	public void redisplayCurrentPhoto() {
		showPhoto(currentPhoto);
	}

}
