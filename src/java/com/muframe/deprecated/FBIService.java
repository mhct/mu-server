package com.muframe.deprecated;

import java.io.File;
import java.io.IOException;

public class FBIService {

	public void runFbi(File photo) throws IOException {
		Runtime.getRuntime().exec("/usr/bin/fbi -T 1 -noverbose -m 1920x1080 -a " + photo.getAbsolutePath() + " &");
	}
	
	public void stopFbi() throws IOException {
		Runtime.getRuntime().exec("/usr/bin/killall fbi");		
	}

	public void turnDisplayOn() throws IOException {
		Runtime.getRuntime().exec("/usr/bin/tvservice -p");		
	}

	public void turnDisplayOff() throws IOException {
		Runtime.getRuntime().exec("/usr/bin/tvservice -o");
	}
	
	
}
