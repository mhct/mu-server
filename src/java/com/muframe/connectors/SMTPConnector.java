package com.muframe.connectors;

import java.io.File;
import java.io.IOException;
import java.util.Properties;

import javax.activation.CommandInfo;
import javax.mail.Folder;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Multipart;
import javax.mail.Session;
import javax.mail.Store;
import javax.mail.internet.MimeBodyPart;
import javax.mail.search.SubjectTerm;

import org.mockito.Mockito;

import com.mongodb.DB;
import com.muframe.server.ServerConnector;

/**
 * Connects to SMTP server an retrieve photos
 * 
 * @author mariohct
 *
 */
public class SMTPConnector implements ServerConnector {
	
	private static final String IMAP_SERVER = "imap.gmail.com";
	private static final String USERNAME = "firstuser.of.muphoto1@gmail.com";
	private static final String PASSWORD = "pass_for_mu";
	
	private DB db;

	public SMTPConnector(DB db) {
		this.db = db;
	}

	public static SMTPConnector getInstance(DB db) {
		if (db == null) {
			throw new IllegalArgumentException("DB can not be null");
		}
		return new SMTPConnector(db);
	}

	@Override
	public void retrievePhotos() {
	    boolean debug = false;

	    try {
		    Properties props = new Properties();
		    props.put("mail.store.protocol", "imaps");
		    Session session = Session.getDefaultInstance(props);
	
		    session.setDebug(debug);
		    Store store = session.getStore("imaps");
		    store.connect(IMAP_SERVER, USERNAME, PASSWORD);
		    
		    Folder inbox = store.getFolder("Inbox");
	        inbox.open(Folder.READ_ONLY);
	        
	        System.out.println("Tentanto");
	        for (Message msg:inbox.search(new SubjectTerm("mu-photo"))) {
	        	System.out.println("!");
	        	System.out.println("Subject:" + msg.getSubject());
	        	
	        	if (msg.getContentType().contains("multipart")) {
	        		for( CommandInfo c: msg.getDataHandler().getAllCommands()) {
	        			System.out.println("command: " + c.getCommandName());
	        		}
	        		Multipart mp = (Multipart) msg.getContent();
	        		for (int i=0; i<mp.getCount(); i++) {
	        			System.out.println("conteudio: " + i);
	        			if (mp.getBodyPart(i).getContentType().contains("multipart")) {
	        				continue;
	        			}
	        			if (mp.getBodyPart(i).getContentType().contains("Multipart")) {
	        				continue;
	        			}
	        			((MimeBodyPart) mp.getBodyPart(i)).saveFile(new File("/tmp/isa.jpg"));
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
	
	public static void main(String[] args) {
		SMTPConnector s = SMTPConnector.getInstance(Mockito.mock(DB.class));
		s.retrievePhotos();
	}
}
