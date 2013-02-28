package com.muframe.server;

import java.net.UnknownHostException;

import org.apache.commons.configuration.Configuration;
import org.apache.commons.configuration.EnvironmentConfiguration;

import com.mongodb.DB;
import com.mongodb.MongoClient;
import com.muframe.connectors.SMTPConnectorTest;

public class MuServer {
	private static final Configuration config = new EnvironmentConfiguration();
	
	private static final String DB_SERVER_IP = config.getString("DB_SERVER_IP", "127.0.0.1");
	private static final Integer DB_SERVER_PORT = config.getInteger("DB_SERVER_PORT", 27017);
	private static final long SLEEPING_TIME = 60000;

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
	
	public static MuServer getInstance(DB db, ServerConnector connector) {
		return new MuServer(db, connector);
	}
	
	public static void main(String[] args) throws UnknownHostException {
		MongoClient dbClient = new MongoClient(DB_SERVER_IP, DB_SERVER_PORT);
		DB db = dbClient.getDB("muphotos");
				
//		MuServer server = MuServer.getInstance(db, SMTPConnectorTest.getInstance(db));
	}
}
