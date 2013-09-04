package com.muframe.server;

import java.io.File;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import com.google.common.collect.Lists;

public class PhotosHolder implements Iterable<File>{
	
	private List<File> photos = Lists.newArrayList();

	public static PhotosHolder getInstance() {
		return new PhotosHolder();
	}


	public void add(File photo) {
		photos.add(photo);
	}


	public int size() {
		return photos.size();
	}


	@Override
	public Iterator<File> iterator() {
		return (new ArrayList<File>(photos)).iterator();
	}

}
