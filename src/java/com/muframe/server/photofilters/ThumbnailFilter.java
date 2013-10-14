package com.muframe.server.photofilters;

import java.io.File;
import java.io.IOException;
import java.util.List;

import org.apache.log4j.Logger;
import org.im4java.core.CommandException;
import org.im4java.core.ConvertCmd;
import org.im4java.core.IM4JavaException;
import org.im4java.core.IMOperation;

public class ThumbnailFilter implements PhotoFilter {

	private static final Logger logger = Logger.getLogger(ThumbnailFilter.class);
	
	@Override
	public void filter(File photo, File output) throws IOException {
		convertPhoto(photo.getAbsolutePath(), output.getAbsolutePath());
	}
	
	private void convertPhoto(String original, String converted) {
		logger.debug("Resizing photo: " + original + " to " + converted);
		ConvertCmd cmd = new ConvertCmd(true);
		IMOperation op = new IMOperation();
		op.thumbnail(640, 360,"^");
		op.addImage(original);
		op.gravity("center");
		op.extent(640, 360);
		
		op.addImage(converted);
		try {
			cmd.run(op);
		} catch (CommandException e) {
            List<String> cmdError = e.getErrorText();
            for (String line:cmdError) {
              System.err.println(line);
            }
		} catch (IOException | InterruptedException | IM4JavaException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			logger.debug("Resize error");
		}
		
		logger.debug("Resize finished");
	}

}
