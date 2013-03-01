package com.muframe.server;

import java.net.UnknownHostException;

import org.apache.commons.configuration.Configuration;
import org.apache.commons.configuration.EnvironmentConfiguration;

import com.mongodb.DB;
import com.mongodb.MongoClient;
import com.muframe.connectors.SMTPConnector;

public class MuServer implements Runnable {
	private static final Configuration config = new EnvironmentConfiguration();
	
	private static final String DB_SERVER_IP = config.getString("DB_SERVER_IP", "127.0.0.1");
	private static final Integer DB_SERVER_PORT = config.getInteger("DB_SERVER_PORT", 27017);
	private static final long SLEEPING_TIME = 6000000;

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
		MongoClient dbClient = new MongoClient(DB_SERVER_IP, DB_SERVER_PORT);
		DB db = dbClient.getDB("muphotos");
				
		Thread server = MuServer.getInstance(db, SMTPConnector.getInstance(db));
		server.start();
	}
}
