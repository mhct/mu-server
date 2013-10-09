package com.muframe.server.photofilters;

import java.io.File;
import java.io.IOException;
import java.util.List;

import org.im4java.core.CommandException;
import org.im4java.core.ConvertCmd;
import org.im4java.core.IM4JavaException;
import org.im4java.core.IMOperation;

public class Resize implements PhotoFilter {

	@Override
	public File filter(File photo) throws IOException {
		convertPhoto(photo.getAbsolutePath(), "/tmp/converted.jpg");
		return null;
	}
	
	private void convertPhoto(String original, String converted) {
		ConvertCmd cmd = new ConvertCmd(true);
		IMOperation op = new IMOperation();
		op.resize(1920, 1080);
		op.addImage(original);
		op.addImage(converted);
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
		}
		
	}

}
