package com.muframe.server;

import java.awt.Cursor;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.GraphicsDevice;
import java.awt.GraphicsEnvironment;
import java.awt.Image;
import java.awt.Point;
import java.awt.Toolkit;
import java.awt.Window;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;

import javax.imageio.ImageIO;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;

public class SwingPhotosDisplay implements PhotosDisplay, Runnable {

	public static void main(String[] args) throws InterruptedException {
		SwingPhotosDisplay spd = new SwingPhotosDisplay();
		Thread t = new Thread(spd);
		t.start();
		
//		Thread.sleep(5000);
//		spd.showPhoto(new File("/tmp/IsaAzul.jpg"));
		
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
	private Image image;
	
	public void showPhoto(final File photo) {
		SwingUtilities.invokeLater(new Runnable() {
			public void run() {
				try {
					image = resize(ImageIO.read(photo));
					repaint();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		});
	}
	
	private Image resize(BufferedImage original) {
//		BufferedImage resizedImage = new BufferedImage(1920, 1080, BufferedImage.TYPE_INT_RGB);
//		Graphics2D g = resizedImage.createGraphics();
//		g.drawImage(original, 0, 0, 1920, 1080, null);
//		g.dispose();
		System.out.println("resize called");
		return original.getScaledInstance(1920, -1, Image.SCALE_FAST);
//		return resizedImage;
	}
	
	public PD() {
		try {
			File logo = new File("/tmp/logo.jpg");
			if (logo.exists()) {
				image = ImageIO.read(new File("/tmp/logo.jpg"));
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	@Override
	protected void paintComponent(Graphics g) {
		super.paintComponent(g);
		if(image != null) {
			g.drawImage(image, 0, 0, null);
		}
	}
}
