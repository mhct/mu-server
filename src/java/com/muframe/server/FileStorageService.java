package com.muframe.server;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import org.apache.log4j.Logger;

/**
 * Provides a interface for persisting and retrieving files from the secondary storage
 * 
 * @author mariohct
 *
 */
public class FileStorageService implements StorageService {
	private static final Logger logger = Logger.getLogger(FileStorageService.class);
	
	private final IDStrategy idGenerator;
	private final String photosFolder;

	private FileStorageService(IDStrategy idGenerator, String photosFolder) {
		this.idGenerator = idGenerator;
		this.photosFolder = photosFolder;
	}
	
	@Override
	public void persist(InputStream in) {
		logger.debug("persist called");
		System.out.println("persist called");
		
		String filename = idGenerator.getId() + ".jpg"; //TODO JPG is fixed now, should get extension from the MIME-TYPE
		OutputStream out = null;
		
		try {
			out = new BufferedOutputStream(new FileOutputStream(new File(photosFolder + File.separator + filename)));
			byte[] buffer = new byte[8192];
			int len;
			
			while ((len = in.read(buffer)) > 0) {
				out.write(buffer, 0, len);
			}
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				if (out != null) {
					out.close();
				}
			}
			catch (IOException e) {
				e.printStackTrace();
			}
			try {
				if (in != null) {
					in.close();
				}
			}
			catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
	
	public static FileStorageService getInstance(IDStrategy idGenerator, String photosFolder) {
		return new FileStorageService(idGenerator, photosFolder);
	}
}
