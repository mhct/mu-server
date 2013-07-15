package com.muframe.server;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.GraphicsDevice;
import java.awt.GraphicsEnvironment;
import java.awt.Image;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;

public class Hello {
    /**
     * Create the GUI and show it.  For thread safety,
     * this method should be invoked from the
     * event-dispatching thread.
     */
    private static void createAndShowGUI() {
        //Create and set up the window.
        JFrame frame = new JFrame("HelloWorldSwing");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        //Add the ubiquitous "Hello World" label.
        JLabel label = new JLabel("Hello World");
        frame.getContentPane().add(label);
        
        MyPanel m = new MyPanel();
        frame.getContentPane().add(m);
        
		GraphicsDevice myDevice = GraphicsEnvironment.getLocalGraphicsEnvironment().getDefaultScreenDevice();
//        myDevice.setFullScreenWindow(m);
		if (myDevice.isFullScreenSupported()) {
			System.out.println("FULL Screen OK");
		} else {
			
			System.out.println("FULL Screen NOT");
		}
        
        //Display the window.
        frame.pack();
        frame.setVisible(true);
        
        
    }

    public static void main(String[] args) {
        javax.swing.SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                createAndShowGUI();
            }
        });
    }
}

class MyPanel extends JPanel {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private Image image;
	
//	public void showPhoto(final File photo) {
//		SwingUtilities.invokeLater(new Runnable() {
//			public void run() {
//				try {
//					image = ImageIO.read(photo);
//					
//					if (image == null) {
//						System.out.println("run: Image is null");
//					} else {
//						System.out.println("Image is NOT null");
//					}
//					repaint();
//				} catch (IOException e) {
//					throw new RuntimeException(e);
//				}
//			}
//		});
//	}
	
	public MyPanel() {
		try {
			image = ImageIO.read(new File("/tmp/logo.png"));
//			File logo = new File("/tmp/logo.png");
//			if (logo.exists()) {
//				image = ImageIO.read(logo);
//			}
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}
	
	@Override
	protected void paintComponent(Graphics g) {
		super.paintComponent(g);
		try {
			if(image != null) {
				g.setColor(Color.BLACK);
				g.fillRect(0, 0, 1920, 1080);
				g.drawImage(image, 20, 20, null);
			} else {
				System.out.println("image is null");
				throw new RuntimeException("image is null");
			}
		} catch (Exception e) {
			System.out.println("some exce;tion inside PD");
			throw new RuntimeException(e);
		}
	}
}