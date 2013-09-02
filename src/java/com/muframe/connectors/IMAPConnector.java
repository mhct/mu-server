package com.muframe.connectors;

import java.io.File;
import java.io.IOException;
import java.util.Properties;

import javax.mail.Flags.Flag;
import javax.mail.Folder;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Multipart;
import javax.mail.Session;
import javax.mail.Store;
import javax.mail.internet.MimeBodyPart;
import javax.mail.search.SearchTerm;

import org.apache.log4j.Logger;

import com.muframe.server.MuServer;
import com.muframe.server.PhotosHolder;
import com.muframe.server.ServerConnector;
import com.muframe.server.StorageService;
import com.muframe.server.UUIDGenerator;
import com.typesafe.config.Config;
import com.typesafe.config.ConfigFactory;

/**
 * Connects to SMTP server an retrieve photos
 * 
 * @author mariohct
 *
 */
public class IMAPConnector implements ServerConnector {
	private static final Logger logger = Logger.getLogger(IMAPConnector.class);
	
	private final Config config = ConfigFactory.load();

	private final String IMAP_SERVER = config.getString("mu-server.imap-connector.imap-server");
	private final String USERNAME = config.getString("mu-server.imap-connector.username");
	private final String PASSWORD = config.getString("mu-server.imap-connector.password");
	
	private StorageService storageService;

	public IMAPConnector(StorageService storageService) {
		this.storageService = storageService;
		
	}

	public static IMAPConnector getInstance(StorageService storageService) {
		if (storageService == null) {
			throw new IllegalArgumentException("DB can not be null");
		}
		return new IMAPConnector(storageService);
	}

	/**
	 * Search criteria for the messages
	 * @return
	 */
	private SearchTerm getSearchQuery() {
		return  new SearchTerm() {
			
			private static final long serialVersionUID = 0103201301L;

			@Override
			public boolean match(Message msg) {
				try {
					if ( ! msg.isSet(Flag.SEEN) && msg.getSubject().matches("[M|m]u-photos?")) {
						return true;
					}
				} catch (MessagingException e) {
					e.printStackTrace();
				}
				return false;
			}
		};
	}
	
	@Override
	public PhotosHolder retrievePhotos() {
		logger.debug("Retrieving photos from IMAP server");
		logger.debug("IMAP server: " + IMAP_SERVER + "\tusername: " + USERNAME);
		
		PhotosHolder photos = PhotosHolder.getInstance();
		
		File photo = null;
		Store store = null;
	    boolean debug = false;

	    try {
		    Properties props = new Properties();
		    props.put("mail.store.protocol", "imaps");
		    Session session = Session.getDefaultInstance(props);
	
		    session.setDebug(debug);
		    store = session.getStore("imaps");
		    store.connect(IMAP_SERVER, USERNAME, PASSWORD);
		    
		    Folder inbox = store.getFolder("Inbox");
	        inbox.open(Folder.READ_WRITE);
	        
	        for (Message msg:inbox.search(getSearchQuery())) {
	        	logger.debug("Found new email having photo(s)");
	        	
	        	if (msg.getContentType().contains("multipart")) {
	        		Multipart mp = (Multipart) msg.getContent();
	        		for (int i=0; i<mp.getCount(); i++) {
	        			if (mp.getBodyPart(i).getContentType().matches("[M|m]ultipart")) {
	        				continue;
	        			}
	        			//TODO identify the file type... running image magick, for example.... to avoid any security attacks, etc.
	        			photo = new File(MuServer.PHOTOS_FOLDER + UUIDGenerator.getInstance().getId() + ".jpg");
	        			((MimeBodyPart) mp.getBodyPart(i)).saveFile(photo);
	        			photos.add(photo);
	        		}
		        }
	        }
	    } catch (MessagingException e) {
	    	e.printStackTrace();
	    } catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			try {
				store.close();
			} catch (MessagingException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	    
	    return photos;
	}
	
}
