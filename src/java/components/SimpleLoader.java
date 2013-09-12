package components;

import java.io.File;

import javax.swing.SwingUtilities;

public class SimpleLoader {
	
	public static void main(String[] args) throws InterruptedException {
		
		final String[] photos = {"/tmp/test1.jpg", "/tmp/test2.jpg"};
		
		SimpleDisplay.createGUI();
    
		Thread m = new Thread(new Runnable() {
			
			@Override
			public void run() {
				for (int i=0; i<10; i++) {
					try {
						Thread.sleep(5000);
					} catch (InterruptedException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					final File f = new File(photos[i%2]);
					SwingUtilities.invokeLater(new Runnable() {
						@Override
						public void run() {
							SimpleDisplay.changePhoto(f);
						}
					});
				}
				
			}
		});
		m.start();
    }
}
