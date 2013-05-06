package com.muframe.server.photofilters;

import java.io.File;
import java.io.IOException;

public class RotatePhotoFilter implements PhotoFilter {

	@Override
	public File filter(File photo) throws IOException {
		String inputFilename = photo.getAbsolutePath();
		String outpuFilename = inputFilename.substring(0, inputFilename.length()-4).concat("_RotatePhotoFilter_").concat(inputFilename.substring(inputFilename.length()-4, inputFilename.length()));
		Runtime.getRuntime().exec("/usr/local/bin/convert -rotate 180 " + inputFilename + " " + outpuFilename);
		
		return new File(outpuFilename);
	}

}
