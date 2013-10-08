package com.muframe.server;

import java.io.File;
import java.io.IOException;

import com.muframe.server.photofilters.PhotoFilter;
import com.muframe.server.photofilters.Resize;

public class SimpleTransf {
	public static void main(String[] args) throws IOException {
		PhotoFilter pf = new Resize();
		pf.filter(new File("/tmp/photo_test.jpg"));
	}
}
