package com.muframe.server.photofilters;

import static org.junit.Assert.*;

import java.io.File;
import java.io.IOException;

import org.junit.Test;

public class RotatePhotoFilterTest {

	@Test
	public void testFilter() throws IOException {
		File regularPhoto = new File("/resources/photo.jpg");
		PhotoFilter pf = new RotatePhotoFilter();
		File rotatedPhoto = pf.filter(regularPhoto);
	}

}
