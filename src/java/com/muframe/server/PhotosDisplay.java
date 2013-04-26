package com.muframe.server;

import java.io.File;

public interface PhotosDisplay {

	public void on();
	public void off();
	public void showPhoto(File photo);
	public void redisplayCurrentPhoto();
}
