package com.muframe.connectors;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

import com.muframe.server.PhotosHolder;
/**
 * Connects to SMTP server an retrieve photos
 * 
 * @author mariohct
 *
 */
public class IMAPConnectorTest {
	
	@Test
	public void connectGmail() {
		//Integration test. First, one should send an email to email configured at application.conf, containing 3 attached photos.
		IMAPConnector conn = IMAPConnector.getInstance();
		PhotosHolder photos = conn.retrievePhotos();
		assertEquals(3, photos.size());
	}
	
	@Test
	public void testStringRegex() {
		String str = "Mu-photos";
		assertTrue(str.matches("[M|m]u-photos?"));
	}
}
