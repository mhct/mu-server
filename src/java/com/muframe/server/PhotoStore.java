package com.muframe.server;

import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import org.apache.commons.configuration.Configuration;
import org.apache.commons.configuration.EnvironmentConfiguration;

/**
 * Persists photo IDs
 * 
 * @author mariohct
 *
 */
public class PhotoStore {
	private static final Configuration config = new EnvironmentConfiguration();
	
	private static final String DB_SERVER_IP = config.getString("DB_SERVER_IP", "127.0.0.1");
	private static final Integer DB_SERVER_PORT = config.getInteger("DB_SERVER_PORT", 27017);
	private static String connectionURL = "jdbc:derby:MuServerDB;create=true";
	private static String dropTable = "DROP TABLE PHOTOS";
	private static String createTable = "CREATE TABLE PHOTOS (photo_id VARCHAR(128) NOT NULL, created_date TIMESTAMP NOT NULL)";
	private String addPhotoId = "INSERT INTO PHOTOS VALUES (?, CURRENT_TIMESTAMP)";
	private Connection conn;
	
	private PhotoStore(Connection conn) {
		this.conn = conn;
	}
	
	/**
	 * Persists a photoId in the database
	 *  
	 * @param photoId
	 */
	public void addPhotoId(String photoId) {
		try {
			PreparedStatement s = conn.prepareStatement(addPhotoId);
			s.setString(1, photoId);
			s.executeUpdate();
		} catch (SQLException e) {
			throw new RuntimeException("Problem adding data to databse: photoID" + photoId + "$ error: " + e);
		}
	}
	
	public static PhotoStore getInstance() {
		return new PhotoStore(PhotoStore.getConnection());
	}

	private static Connection getConnection() {
		Connection conn = null;
		try {
//			Class.forName("org.apache.derby.jdbc.EmbeddedDriver");
			conn = DriverManager.getConnection(connectionURL);
		}
		catch(SQLException e) {
			System.out.println("Error getting database connection. error: " + e);
			System.out.println(e.getNextException());
		}
		
		return conn;
	}
	
	/**
	 * Create the needed tables in the case they still do not exist in the database
	 */
	public static void initializeStore() {
		Connection conn = getConnection();
		
		DatabaseMetaData metaData;
		try {
			if (conn != null) {
				metaData = conn.getMetaData();
				ResultSet rs = metaData.getTables(null, null, "PHOTOS", null);
				if (!rs.next()) {
					Statement s = conn.createStatement();
					s.execute(createTable);
				}
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			try {
				if (conn != null) {
					conn.close();
				}
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
	
	public static void dropStore() {
		Connection conn = getConnection();
		
		try {
			Statement s = conn.createStatement();
			s.execute(dropTable);
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	public String getLastPhotoId() {
		String photoId = null;
		try {
			Statement s = conn.createStatement();
			s.execute("SELECT photo_id FROM PHOTOS ORDER BY created_date DESC");
			ResultSet result = s.getResultSet();
			if (result.next()) {
				photoId = result.getString("photo_id");
			}
		} catch (SQLException e) {
			throw new RuntimeException("Cant select photo. Error: " + e);
		}
		return photoId;
	}
}