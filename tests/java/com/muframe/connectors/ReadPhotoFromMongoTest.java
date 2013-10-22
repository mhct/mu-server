//package com.muframe.connectors;
//
//import java.io.File;
//import java.io.IOException;
//
//import com.mongodb.DB;
//import com.mongodb.MongoClient;
//import com.mongodb.gridfs.GridFS;
//import com.mongodb.gridfs.GridFSDBFile;
//
//public class ReadPhotoFromMongoTest {
//
//	/**
//	 * @param args
//	 * @throws IOException 
//	 */
//	public static void main(String[] args) throws IOException {
//		MongoClient client = new MongoClient("127.0.0.1", 27017);
//		DB db = client.getDB("muphotos");
////		BasicDBObject photo = (BasicDBObject) db.getCollection("photos").findOne();
////		BSONObject photo = (BSONObject) db.getCollection("photos").findOne();
//		GridFS gridfs = new GridFS(db);
//		GridFSDBFile file = gridfs.findOne("876c2bd8-b924-4307-9c0f-57f22dc29315");
//		
//		
//		//		DBObject oo = (DBObject) db.getCollection("photos").findOne();
//		
////		Binary obj = (Binary) photo.get("photoID");
//		
////		Object obj = photo.get("photoID");
//		
////		System.out.println(obj.toString());
//		File f = new File("/tmp/876c2bd8-b924-4307-9c0f-57f22dc29315.jpg");
//		file.writeTo(f);
//	}
//
//}
