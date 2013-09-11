package com.muframe.server;

import java.awt.Color;
import java.awt.Cursor;
import java.awt.Graphics;
import java.awt.GraphicsDevice;
import java.awt.GraphicsEnvironment;
import java.awt.Image;
import java.awt.Point;
import java.awt.Toolkit;
import java.awt.image.BufferedImage;
import java.awt.image.ImageObserver;
import java.io.File;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.util.Observable;
import java.util.Observer;

import javax.imageio.ImageIO;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;

import org.apache.log4j.Logger;

public class SwingPhotosDisplay implements PhotosDisplay, Runnable {
	private static final Logger logger = Logger.getLogger(SwingPhotosDisplay.class);
	
	//FOR simple testing purposes.. TODO create unit test for this code
	public static void main(String[] args) throws InterruptedException {
//		SwingPhotosDisplay spd = new SwingPhotosDisplay();
//		Thread t = new Thread(spd);
//		t.start();
		
		PhotosDisplay spd = SwingPhotosDisplay.getInstance();
		
		if (args.length > 0) {
			spd.showPhoto(new File(args[0]));
			Thread.sleep(30000);
		}
		
		Thread.sleep(5000);
		spd.showPhoto(new File("/tmp/test1.jpg"));

		Thread.sleep(30000);
		spd.showPhoto(new File("/tmp/test2.jpg"));
		
		
		
	}

	public static PhotosDisplay getInstance() {
		SwingPhotosDisplay spd = new SwingPhotosDisplay();
		Thread t = new Thread(spd);
		t.start();
		
		return spd;
	}
	
	private GraphicsDevice myDevice;
	private JFrame myWindow;
	private PD pd;
	private File currentPhoto;
	
	SwingPhotosDisplay() {
		try {
			SwingUtilities.invokeAndWait(new Runnable() {
				public void run() {
					createAndShowGUI();
				}
			});
		} catch (InvocationTargetException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	private void createAndShowGUI() {
		myDevice = GraphicsEnvironment.getLocalGraphicsEnvironment().getDefaultScreenDevice();
		myWindow = new JFrame();
		BufferedImage cursorImg = new BufferedImage(16, 16, BufferedImage.TYPE_INT_ARGB);
		
		// Create a new blank cursor.
		Cursor blankCursor = Toolkit.getDefaultToolkit().createCustomCursor(
				cursorImg, new Point(0, 0), "blank cursor");
		
		// Set the blank cursor to the JFrame.
		myWindow.getContentPane().setCursor(blankCursor);
		
		pd = new PD();
		myWindow.add(pd);
	}
	
	private void eventLoop() {
		boolean change = true;
		for (;;) {
			try {
				Thread.sleep(5000);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
			if (change) {
				change = false;
			} else {
				change = true;
			}
		}
		
	}
	
	@Override
	public void run() {
//		eventLoop();
		if (myDevice.isFullScreenSupported()) {
			try {
				myDevice.setFullScreenWindow(myWindow);
				eventLoop();
			} finally {
				myDevice.setFullScreenWindow(null);
			}
		}
	}
	
	@Override
	public void on() {
		// TODO Auto-generated method stub

	}

	@Override
	public void off() {
		// TODO Auto-generated method stub

	}

	@Override
	public void showPhoto(File photo) {
		setCurrentPhoto(photo);
		showCurrentPhoto();
	}

	@Override
	public void redisplayCurrentPhoto() {
		showCurrentPhoto();
	}
	
	private void showCurrentPhoto() {
		pd.showPhoto(currentPhoto);
	}

	private void setCurrentPhoto(File photo) {
		currentPhoto = photo;
	}


}

class PD extends JPanel {
	private static final Logger logger = Logger.getLogger(PD.class);
	private final ImageObserver il = new ImageLogger();
	private Image image;
	
	public PD() {
		try {
			File logo = new File("/tmp/logo.png");
			if (logo.exists()) {
				image = ImageIO.read(logo);
				if (image == null) {
					logger.debug("image is null");
				}
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
//			e.printStackTrace();
			logger.error(e);
		}
	}
	
	public void showPhoto(final File photo) {
		logger.debug("PD.showPhoto invoked");
		
		SwingUtilities.invokeLater(new Runnable() {
			public void run() {
				try {
					logger.debug("Runnable.run() invoked");
					
//					image = resize(ImageIO.read(photo));
					image = ImageIO.read(photo);
					if (image == null) {
						logger.debug("image is null. Photo file: " + photo.getAbsolutePath());
					} else {
						logger.debug("Repaint() before");
						repaint();
						logger.debug("Repaint() AFTER");
					}
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		});
	}
	
//	private Image resize(BufferedImage original) {
//		BufferedImage resizedImage = new BufferedImage(1920, 1080, BufferedImage.TYPE_INT_RGB);
//		Graphics2D g = resizedImage.createGraphics();
//		g.drawImage(original, 0, 0, 1920, 1080, null);
//		g.dispose();
//		System.out.println("resize called");
//		return original.getScaledInstance(1920, -1, Image.SCALE_SMOOTH);
//		return resizedImage;
//	}
	
	
	
	@Override
	protected void paintComponent(Graphics g) {
		super.paintComponent(g);
		if(image != null) {
			g.setColor(Color.BLACK);
			g.fillRect(0, 0, 1920, 1080);
			g.drawImage(image, 0, 0, il);
		}
	}
}

class ImageLogger implements ImageObserver {
	private static final Logger logger = Logger.getLogger(ImageObserver.class);
	
	@Override
	public boolean imageUpdate(Image img, int infoflags, int x, int y,
			int width, int height) {

		logger.debug("Current img: " + img.toString() + ".infoFlags:" + infoflags);

		return false;
	}
	
}