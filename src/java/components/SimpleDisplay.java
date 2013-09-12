package components;

import java.awt.Color;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.Point;
import java.awt.Toolkit;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;

import org.apache.log4j.Logger;

public class SimpleDisplay extends JPanel {
	private static final Logger logger = Logger.getLogger(SimpleDisplay.class);
	private static final long serialVersionUID = 20131011L;
	private static SimpleDisplay fucking;
	private JLabel labelPhoto;

	public SimpleDisplay() throws IOException {
        super(new GridLayout(1,1));

        ImageIcon icon = new ImageIcon(SimpleDisplay.class.getResource("/logo.jpg"));

        labelPhoto = new JLabel(null,
                            icon,
                            JLabel.CENTER);

        labelPhoto = new JLabel();
        labelPhoto.setBackground(Color.BLACK);
        labelPhoto.setHorizontalAlignment(JLabel.CENTER);
        setBackground(Color.BLACK);
        setBounds(0, 0, 1920, 1080);

        add(labelPhoto);
    }

	public static void changePhoto(File photo) {
		logger.debug("changePhoto called. : " + photo);
		try {
			fucking.labelPhoto.setIcon(createImageIcon(photo));
		} catch (IOException e) {
			logger.debug("Fucking exception" + e);
			e.printStackTrace();
		}
	}
	
    protected static ImageIcon createImageIcon(File photo) throws IOException {
    	BufferedImage img = ImageIO.read(photo);
        if (img != null) {
            return new ImageIcon(img);
        } else {
            System.err.println("Couldn't find file: " + photo.getAbsolutePath());
            return null;
        }
    }

    private static void createAndShowGUI() throws IOException, URISyntaxException {
        JFrame frame = new JFrame("SimplePhotosDisplay");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(new Dimension(1920, 1080));
        
		// Create a new blank cursor.
		BufferedImage cursorImg = new BufferedImage(16, 16, BufferedImage.TYPE_INT_ARGB);
		Cursor blankCursor = Toolkit.getDefaultToolkit().createCustomCursor(cursorImg, new Point(0, 0), "blank cursor");
		frame.getContentPane().setCursor(blankCursor);

		
        fucking = new SimpleDisplay();
        frame.add(fucking);

        frame.setVisible(true);
    }
    

	public static void createGUI() throws InterruptedException {
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
				try {
					createAndShowGUI();
					//loads logo (TODO fix this.. it is a hack)
					changePhoto(new File(SimpleDisplay.class.getResource("/logo.jpg").toURI()));
				} catch (IOException e) {
					e.printStackTrace();
				} catch (URISyntaxException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
            }
        });
	}
    
	public static void main(String[] args) throws InterruptedException {
		createGUI();
        
        Thread.sleep(10000);
        SwingUtilities.invokeLater(new Runnable() {
			@Override
			public void run() {
				changePhoto(new File("/tmp/test2.jpg"));
			}
		});
    }

}