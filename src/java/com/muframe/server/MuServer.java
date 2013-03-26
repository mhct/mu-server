package com.muframe.server;

import java.net.UnknownHostException;

import org.apache.commons.configuration.Configuration;
import org.apache.commons.configuration.EnvironmentConfiguration;
import org.mockito.Mockito;

import com.mongodb.DB;
import com.mongodb.MongoClient;
import com.muframe.connectors.IMAPConnector;

public class MuServer implements Runnable {
	private static final Configuration config = new EnvironmentConfiguration();
	
	private static final String DB_SERVER_IP = config.getString("DB_SERVER_IP", "127.0.0.1");
	private static final Integer DB_SERVER_PORT = config.getInteger("DB_SERVER_PORT", 27017);
	public static final String PHOTOS_FOLDER = config.getString("PHOTOS_FOLDER", "/home/pi/mu-server/show_photos/");
	
	private static final long SLEEPING_TIME = 60000; //miliseconds


	private DB db;
	private ServerConnector connector;
	
	/**
	 * main run-loop, check apache-daemon for this
	 */
	public void run() {
		for(;;) {
			connector.retrievePhotos();
			try {
				Thread.sleep(SLEEPING_TIME);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}
	
	private MuServer(DB db, ServerConnector connector) {
		if (db == null || connector == null) {
			throw new IllegalArgumentException("DB or Connector can not be null");
		}
		
		this.db = db;
		this.connector = connector;
	}
	
	public static Thread getInstance(DB db, ServerConnector connector) {
		return new Thread(new MuServer(db, connector));
	}
	
	public static void main(String[] args) throws UnknownHostException {
//		MongoClient dbClient = new MongoClient(DB_SERVER_IP, DB_SERVER_PORT);
//		DB db = dbClient.getDB("muphotos");
				
		Thread server = MuServer.getInstance(Mockito.mock(DB.class), IMAPConnector.getInstance(FileStorageService.getInstance(UUIDGenerator.getInstance(), PHOTOS_FOLDER)));
		server.start();
	}
}
