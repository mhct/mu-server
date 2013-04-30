package com.muframe.server;

import java.io.File;
import java.io.IOException;

import org.apache.log4j.Logger;

public class FBIPhotosDisplay implements Runnable, PhotosDisplay {
	private static final Logger logger = Logger.getLogger(FBIPhotosDisplay.class);
	
	private File newPhoto;
	private File currentPhoto;
	private final long  SLEEPING_TIME = 1000; //1 second

	private boolean showNewPhoto;

	private boolean displayOn;

	private FBIService service;

	private boolean shouldTurnScreenOn = false;

	private boolean shouldTurnScreenOff = false;

	FBIPhotosDisplay(FBIService service) {
		this.service = service;
		displayOn = true;
	}
	
	public static PhotosDisplay getInstance() {
		FBIPhotosDisplay display = new FBIPhotosDisplay(new FBIService());
		Thread thread = new Thread(display);
		thread.start();
		
		return (PhotosDisplay) display;
	}
	
	@Override
	public void run() {
		for(;;) {
			try {
				if (shouldTurnScreenOn && displayOn == false) {
					shouldTurnScreenOn = false;
					displayOn = true;
					service.turnDisplayOn();
					redisplayCurrentPhoto();
				}
				if (shouldTurnScreenOff && displayOn == true) {
					shouldTurnScreenOff = false;
					displayOn = false;
					service.turnDisplayOff();
				}
			
				if ( newPhoto != null && showNewPhoto){
//				logger.debug("Received new photo do be displayed");
					System.out.println("\nReceived new photo do be displayed");
						killCurrentPhoto();
						showNewPhoto();
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
			
			//sleeping block
			try {
				Thread.sleep(SLEEPING_TIME);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}
	
	private void showNewPhoto() throws IOException {
//		logger.debug("Loading new fbi process");
		System.out.println("Loading new fbi process");
		currentPhoto = newPhoto;
		newPhoto = null;
		showNewPhoto = false;
		service.runFbi(currentPhoto);
	}

	private void killCurrentPhoto() throws IOException {
		System.out.println("Killing current FBI process");
//		logger.debug("Killing current FBI process");
		service.stopFbi();
	}

	public void showPhoto(File photo) {
		synchronized(this){
			newPhoto = photo;
			showNewPhoto = true;
		}
	}
	
	public void redisplayCurrentPhoto() {
		if (currentPhoto != null) {
			showPhoto(currentPhoto);
		}
	}

	public void on() {
		synchronized(this){
			shouldTurnScreenOn = true;
		}
	}

	public void off() {
		synchronized(this) {
			shouldTurnScreenOff = true;
		}
	}
	
	

}
