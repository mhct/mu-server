package com.muframe.connectors;

import static org.junit.Assert.assertTrue;

import org.junit.Test;
import org.mockito.Mockito;

import com.muframe.server.StorageService;
/**
 * Connects to SMTP server an retrieve photos
 * 
 * @author mariohct
 *
 */
public class SMTPConnectorTest {
	
	@Test
	public void connectGmail() {
		IMAPConnector conn = IMAPConnector.getInstance(Mockito.mock(StorageService.class));
		conn.retrievePhotos();
//		assertNotNull(f);
	}
	
	@Test
	public void testStringRegex() {
		String str = "Mu-photos";
		assertTrue(str.matches("[M|m]u-photos?"));
	}
}
