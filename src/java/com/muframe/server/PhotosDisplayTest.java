package com.muframe.server;

import java.io.File;

public class PhotosDisplayTest {

	public static void main(String[] args) {
		PhotosDisplay d = PhotosDisplay.getInstance();
		d.showPhoto(new File("/tmp/foto.jpg"));
	}

}
