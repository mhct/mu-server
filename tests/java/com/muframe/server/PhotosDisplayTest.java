package com.muframe.server;

import java.io.File;
import java.io.IOException;

import org.junit.Test;

public class PhotosDisplayTest {

	@Test
	public void testFbiDisplay() throws InterruptedException {
		FBIPhotosDisplay display = new FBIPhotosDisplay(new FakeFBIService());
		Thread thread = new Thread(display);
		thread.start();

		display.showPhoto(new File("."));
		display.off();
		display.on();
		Thread.sleep(2000);
		System.out.println("porra");
		display.off();
//		display.off();
//		display.off();
	}

}

class FakeFBIService extends FBIService {
	public void runFbi(File photo) throws IOException {
		System.out.println("fbi called");
	}
	
	public void stopFbi() throws IOException {
		System.out.println("kill FBI called");
	}

	public void turnDisplayOn() throws IOException {
		System.out.println("Turn ON display called");
	}

	public void turnDisplayOff() throws IOException {
		System.out.println("Turn OFF display called");
	}
}