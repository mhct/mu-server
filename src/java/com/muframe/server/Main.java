package com.muframe.server;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.GraphicsEnvironment;
import java.awt.HeadlessException;
import java.awt.Image;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.JFrame;
import javax.swing.JPanel;

public class Main {
	public static void main(String[] args) throws IOException {
		String file = "";
		if (args.length == 1) {
			file = args[0];
		}
		else {
			file = "/tmp/foto.jpg";
		}
		
		final JFrame frame = new ImageFrame("teste", file);
		frame.setVisible(true);

//		Boolean b  = GraphicsEnvironment.getLocalGraphicsEnvironment().getDefaultScreenDevice().isFullScreenSupported();
		try {
			GraphicsEnvironment.getLocalGraphicsEnvironment().getDefaultScreenDevice().setFullScreenWindow(frame);
		}
		catch (HeadlessException e) {
			e.printStackTrace();
		}
		
		
	}
}

class ImageFrame extends JFrame {
	
	private static final long serialVersionUID = 1L;

	public ImageFrame(String title, String file) {
		super(title);
		
		this.setLayout(new BorderLayout());
		this.setDefaultCloseOperation(DISPOSE_ON_CLOSE);
		this.setUndecorated(true);
//		this.add(new JLabel(new ImageIcon("/tmp/foto.jpg")));
		this.add(new ImagePanel(file));
//		this.pack();
	}
	
//	private 
//	Image scaledImage = img.getScaledInstance((int)width, (int)height, Image.SCALE_SMOOTH);
//    BufferedImage imageBuff = new BufferedImage((int)width, (int)height, BufferedImage.TYPE_INT_RGB);
//    Graphics g = imageBuff.createGraphics();
//    g.drawImage(scaledImage, 0, 0, new Color(0,0,0), null);
//    g.dispose();
}

//GraphicsDevice myDevice;
//Window myWindow;
//
//try {
//    myDevice.setFullScreenWindow(myWindow);
//    ...
//} finally {
//    myDevice.setFullScreenWindow(null);
//}
class ImagePanel extends JPanel {

	private static final long serialVersionUID = 2013022401L;
	private Image image;

    public ImagePanel(String file) {
       try {                
//          image = ImageIO.read(new File(file)).getScaledInstance(1920, 1080, Image.SCALE_DEFAULT);
          image = ImageIO.read(new File(file));
       } catch (IOException e) {
            // handle exception...
    	   e.printStackTrace();
       }
    }

    @Override
    public Dimension getPreferredSize() {
        return new Dimension(1920, 1080);
    }
    
    /* (non-Javadoc)
     * @see javax.swing.JComponent#paintComponent(java.awt.Graphics)
     */
    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2 = (Graphics2D) g; 
//        System.out.println(1920/image.getWidth(null));
//        g2.scale(1920.0/(double)image.getWidth(null), 1080.0/(double)image.getHeight(null));
//        g2.scale(2, 2);
        g2.drawImage(image, 0, 0, this); // see javadoc for more info on the parameters            
    }

}