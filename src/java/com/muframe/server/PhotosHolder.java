package com.muframe.server;

import java.io.File;
import java.util.List;

import com.google.common.collect.Lists;

public class PhotosHolder {
	private List<File> photos = Lists.newArrayList();

	public static PhotosHolder getInstance() {
		return new PhotosHolder();
	}


	public void add(File photo) {
		photos.add(photo);
	}

}
