package com.muframe.server.photofilters;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.List;

import org.apache.log4j.Logger;
import org.im4java.core.CommandException;
import org.im4java.core.ConvertCmd;
import org.im4java.core.IM4JavaException;
import org.im4java.core.IMOperation;

public class Resize implements PhotoFilter {

	private static final Logger logger = Logger.getLogger(Resize.class);
	
	@Override
	public void filter(File photo, File output) throws IOException {
		if (photo == null || !photo.exists() || !photo.isFile()) {
			throw new RuntimeException("Photo can not be null/empty");
		}
		
		if (photo.getAbsolutePath().endsWith(".gif")) {
			logger.debug("Simple copy of" + photo.getAbsolutePath() + " to " + output.getAbsolutePath());
			Files.copy(photo.toPath(), output.toPath());
		} else {
			convertPhoto(photo.getAbsolutePath(), output.getAbsolutePath());
		}
	}
	
	private void convertPhoto(String original, String converted) {
		logger.debug("Resizing photo: " + original + " to " + converted);
		final long start = System.currentTimeMillis();
		
		ConvertCmd cmd = new ConvertCmd(true);
		cmd.setSearchPath("/usr/local/bin");
		IMOperation op = new IMOperation();
		op.resize(1920, 1080,"^");
		op.autoOrient();
		op.addImage(original);
		op.gravity("center");
		op.extent(1920, 1080);
		
		op.addImage(converted);
		System.out.println(op.toString());
		try {
			cmd.run(op);
		} catch (CommandException e) {
            List<String> cmdError = e.getErrorText();
            for (String line:cmdError) {
              System.err.println(line);
            }
            e.printStackTrace();
		} catch (IOException | InterruptedException | IM4JavaException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			logger.debug("Resize error");
		}
		
		logger.debug("Resize finished in: " + (System.currentTimeMillis() - start) + " ms");
	}

}
