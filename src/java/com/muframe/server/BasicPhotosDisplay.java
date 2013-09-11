package com.muframe.server;

import java.awt.BorderLayout;
import java.awt.Cursor;
import java.awt.FlowLayout;
import java.awt.GraphicsDevice;
import java.awt.GraphicsEnvironment;
import java.awt.Point;
import java.awt.Toolkit;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;

public class BasicPhotosDisplay extends JFrame implements PhotosDisplay {

	private static final long serialVersionUID = 20131011L;

	public BasicPhotosDisplay() throws IOException {
		
	}
	
	public void createScreen() {
		BufferedImage img = null;
		try {
			img = ImageIO.read(new File("/tmp/isa.jpg"));
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		ImageIcon icon = new ImageIcon(img);
		final JLabel label = new JLabel(icon);

		final JPanel contentPane = new JPanel(new BorderLayout());
		contentPane.setBorder(BorderFactory.createEmptyBorder());
		contentPane.add(label);
		
		
		// Create a new blank cursor.
		BufferedImage cursorImg = new BufferedImage(16, 16, BufferedImage.TYPE_INT_ARGB);
		Cursor blankCursor = Toolkit.getDefaultToolkit().createCustomCursor(cursorImg, new Point(0, 0), "blank cursor");
		getContentPane().setCursor(blankCursor);
		
		setContentPane(contentPane);
	}
	
	private static void fullScreen(final GraphicsDevice device, JFrame window) {
		if (device.isFullScreenSupported()) {
			try {
				device.setFullScreenWindow(window);
			} finally {
				device.setFullScreenWindow(null);
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

		
	}

	@Override
	public void redisplayCurrentPhoto() {
		// TODO Auto-generated method stub

	}
	
	public static void main(final String[] args) throws IOException {
		
		
		
		SwingUtilities.invokeLater(new Runnable() {
            public void run() {
            	GraphicsDevice myDevice = GraphicsEnvironment.getLocalGraphicsEnvironment().getDefaultScreenDevice();
            	BasicPhotosDisplay app;
				try {
					app = new BasicPhotosDisplay();
					if (args.length > 0 ) {
						fullScreen(myDevice, app);
					}
					app.createScreen();
					app.setVisible(true);
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
            	
            }
        });
	}

}
