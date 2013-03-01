package com.muframe.connectors;

import java.io.BufferedOutputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

import org.bson.types.Binary;

import com.mongodb.BasicDBObject;
import com.mongodb.DB;
import com.mongodb.DBObject;
import com.mongodb.MongoClient;

public class ReadPhotoFromMongoTest {

	/**
	 * @param args
	 * @throws IOException 
	 */
	public static void main(String[] args) throws IOException {
		MongoClient client = new MongoClient("127.0.0.1", 27017);
		DB db = client.getDB("muphotos");
		BasicDBObject photo = (BasicDBObject) db.getCollection("photos").findOne();
		
		Object obj = photo.get("photoID");
		
//		Object obj = photo.get("photoID");
		
//		System.out.println(obj.toString());
		File f = new File("/tmp/bla.jpg");
		
		BufferedOutputStream output = new BufferedOutputStream(new FileOutputStream(f));
		output.write(obj.toString().getBytes());
		output.close();
	}

}
