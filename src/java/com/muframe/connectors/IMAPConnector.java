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
import com.muframe.server.ServerConnector;
import com.muframe.server.StorageService;
import com.muframe.server.UUIDGenerator;

/**
 * Connects to SMTP server an retrieve photos
 * 
 * @author mariohct
 *
 */
public class IMAPConnector implements ServerConnector {
	private static final Logger logger = Logger.getLogger(IMAPConnector.class);
	
//	private static final String IMAP_SERVER = "imap.gmail.com";
//	private static final String USERNAME = "firstuser.of.muphoto1@gmail.com";
//	private static final String PASSWORD = "pass_for_mu";
	private static final String IMAP_SERVER = "127.0.0.1";
	private static final String USERNAME = "pi";
	private static final String PASSWORD = "donottrytoblueme";
	
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
					if (! msg.isSet(Flag.SEEN) && msg.getSubject().contains("mu-photo")) {
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
	public void retrievePhotos() {
		logger.debug("Retrieving photos from IMAP server");
		logger.debug("IMAP server: " + IMAP_SERVER + "\tusername: " + USERNAME);
		
	    boolean debug = false;

	    try {
		    Properties props = new Properties();
//		    props.put("mail.store.protocol", "imaps");
		    props.put("mail.store.protocol", "imap");
		    Session session = Session.getDefaultInstance(props);
	
		    session.setDebug(debug);
//		    Store store = session.getStore("imaps");
		    Store store = session.getStore("imap");
		    store.connect(IMAP_SERVER, USERNAME, PASSWORD);
		    
		    Folder inbox = store.getFolder("Inbox");
	        inbox.open(Folder.READ_WRITE);
	        
	        for (Message msg:inbox.search(getSearchQuery())) {
	        	
	        	if (msg.getContentType().contains("multipart")) {
	        		Multipart mp = (Multipart) msg.getContent();
	        		for (int i=0; i<mp.getCount(); i++) {
	        			if (mp.getBodyPart(i).getContentType().contains("multipart")) {
	        				continue;
	        			}
	        			if (mp.getBodyPart(i).getContentType().contains("Multipart")) {
	        				continue;
	        			}
	        			((MimeBodyPart) mp.getBodyPart(i)).saveFile(new File(MuServer.PHOTOS_FOLDER + UUIDGenerator.getInstance().getId() + ".jpg"));
//	        			storageService.persist(((MimeBodyPart) mp.getBodyPart(i)).getInputStream());
	        			
	        		}
		        }
	        }
	    } catch (MessagingException e) {
	    	e.printStackTrace();
	    } catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}
	
//	private void savePhoto(InputStream in) throws IOException {
//		int bufferSize = 1024 * 512; // Average picture size
//		byte[] buffer = new byte[8192];
//		ByteArrayOutputStream out = new ByteArrayOutputStream(bufferSize);
//		
//		try {
//			int len;
//			while ((len = in.read(buffer)) > 0) {
//				out.write(buffer);
//			}
////			Binary b = new Binary(out.toByteArray());
////			db.getCollection("photos").insert(new BasicDBObject("photoID", new Binary(out.toByteArray())));
//			String filename = UUID.randomUUID().toString();
//			//ADD META DATA.. about the file, timestamp, etc.
//			System.out.println(filename);
//			GridFS savefs = new GridFS(db);
//			GridFSInputFile file = savefs.createFile(out.toByteArray());
//			file.setFilename(filename);
//			file.save();
//			
//		} catch (IOException e) {
//			throw new IOException(e);
//		} finally {
//			out.close();
//		}
//	}

//	public static void main(String[] args) {
//		IMAPConnector s = IMAPConnector.getInstance(FileStorageService.getInstance(UUIDGenerator.getInstance()));
//		s.retrievePhotos();
//	}
}
