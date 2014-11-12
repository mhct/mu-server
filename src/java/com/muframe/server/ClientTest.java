package com.muframe.server;

import java.io.IOException;
import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;

import retrofit.RestAdapter;
import retrofit.client.Response;

import com.example.helloworld.core.Photo;
import com.muframe.connectors.PhotosResourceAPI;

public class ClientTest {

	public static void main(String[] args) throws IOException {
		RestAdapter adapter = new RestAdapter.Builder().setEndpoint("http://localhost:8080").build();
		
		PhotosResourceAPI service = adapter.create(PhotosResourceAPI.class);
		
		Photo photo = service.getLatestPhotoId("mario");
		
//		URL url = null;
//		for (Photo p: photos) {
//			
//			System.out.println("." + p.getUrl());
//			url = p.getUrl();
//		}
		
		String filename = photo.getUrl().toString().substring(photo.getUrl().toString().lastIndexOf("/"));
		Response res = service.getPhotoContent("mario", filename);
		Path path = FileSystems.getDefault().getPath("/tmp", "1bla.jpg");
		Files.copy(res.getBody().in(), path, StandardCopyOption.REPLACE_EXISTING);
		
	}
}
