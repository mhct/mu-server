package com.muframe.server.photofilters;

import java.io.File;
import java.io.IOException;

import org.junit.Test;

public class ResizeTest {

	@Test
	public void testFilter() throws IOException {
		
		PhotoFilter pf = new Resize();
		pf.filter(new File("resources/photo_test.jpg"));
	}
	
	public static void main(String[] args) throws IOException {
		PhotoFilter pf = new Resize();
		pf.filter(new File("/tmp/photo_test.jpg"));
	}

}
