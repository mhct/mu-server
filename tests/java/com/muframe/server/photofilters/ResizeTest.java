package com.muframe.server.photofilters;

import static org.junit.Assert.*;

import java.io.File;
import java.io.IOException;

import org.junit.Test;

public class ResizeTest {

	@Test(expected=RuntimeException.class)
	public void emptyPhoto() throws IOException {
		PhotoFilter resize = new Resize();
		File photo = new File("/tmp/ffffffffdfasdf.jpg");
		File output = new File("/tmp/078-out.jpg");
		
		resize.filter(photo, output);
	}
	
	@Test
	public void test() throws IOException {
		PhotoFilter resize = new Resize();
		File photo = new File("/tmp/down.jpg");
		File output = new File("/tmp/down-out.jpg");
		
		resize.filter(photo, output);
		assertTrue(output.exists());
	}

}
