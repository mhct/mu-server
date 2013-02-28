package com.muframe.connectors;

import org.junit.Test;
import org.mockito.Mockito;

import com.mongodb.DB;

/**
 * Connects to SMTP server an retrieve photos
 * 
 * @author mariohct
 *
 */
public class SMTPConnectorTest {
	
	@Test
	public void connectGmail() {
		SMTPConnector conn = new SMTPConnector(Mockito.mock(DB.class));
		conn.retrievePhotos();
	}
}
