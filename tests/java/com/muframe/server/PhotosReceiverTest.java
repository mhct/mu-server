package com.muframe.server;

import java.io.BufferedInputStream;
import java.io.ByteArrayOutputStream;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;

import com.muframe.connectors.PhotosManipulationClient;

import retrofit.RestAdapter;
import retrofit.mime.TypedByteArray;

public class PhotosReceiverTest {

	public static void main(String[] args) throws FileNotFoundException {
		InputStream bis = new BufferedInputStream(new FileInputStream("/tmp/1bla.jpg"));
		byte[] imageData = readAndClose(bis);
		
		RestAdapter adapter = new RestAdapter.Builder().setEndpoint("http://localhost:9090").build();
		
		PhotosManipulationClient service = adapter.create(PhotosManipulationClient.class);
		TypedByteArray photo = new TypedByteArray("multipart/form-data", imageData);
		
		Object res = service.createPhoto("mario", photo, "blabla.jpg");
	
	}

	static byte[] readAndClose(InputStream aInput){
	    //carries the data from input to output :    
	    byte[] bucket = new byte[32*1024]; 
	    ByteArrayOutputStream result = null; 
	    try  {
	      try {
	        //Use buffering? No. Buffering avoids costly access to disk or network;
	        //buffering to an in-memory stream makes no sense.
	        result = new ByteArrayOutputStream(bucket.length);
	        int bytesRead = 0;
	        while((bytesRead = aInput.read(bucket)) != -1){
	          if(bytesRead > 0){
	            result.write(bucket, 0, bytesRead);
	          }
	        }
	      }
	      finally {
	        aInput.close();
	        //result.close(); this is a no-operation for ByteArrayOutputStream
	      }
	    }
	    catch (IOException ex){
	    	ex.printStackTrace();
	    }
	    return result.toByteArray();
	  }

}
