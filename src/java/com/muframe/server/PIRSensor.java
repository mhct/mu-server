package com.muframe.server;

import java.io.File;

import org.apache.log4j.Logger;

import com.pi4j.io.gpio.GpioController;
import com.pi4j.io.gpio.GpioFactory;
import com.pi4j.io.gpio.GpioPinDigitalInput;
import com.pi4j.io.gpio.RaspiPin;
import com.pi4j.io.gpio.event.GpioPinDigitalStateChangeEvent;
import com.pi4j.io.gpio.event.GpioPinListenerDigital;

public class PIRSensor implements Runnable {
	private static final Logger logger = Logger.getLogger(PIRSensor.class);

	private static final long TIME_TO_TURN_OFF = 120000; // 2 minutes TODO configuration file?!
	
	private final long  SLEEPING_TIME = 1000; //1 second

	private final PhotosDisplay display;

	private int passedTime;

	private PIRSensor(final PhotosDisplay display) {
		this.display = display;
		
		final GpioController gpio = GpioFactory.getInstance();
		GpioPinDigitalInput myButton = gpio.provisionDigitalInputPin(RaspiPin.GPIO_06, "pin22");
		myButton.addListener(new GpioPinListenerDigital() {

			@Override
			public void handleGpioPinDigitalStateChangeEvent(GpioPinDigitalStateChangeEvent event) {
				System.out.println(" ---> GPIO " + event.getPin() + " state changed: " + event.getState());
				logger.debug(" ---> GPIO " + event.getPin() + " state changed: " + event.getState());
//				display.redisplayCurrentPhoto();
				display.on();
				passedTime = 0;
			}
		});
	}
	
	public static PIRSensor getInstance(PhotosDisplay display) {
		PIRSensor pir = new PIRSensor(display);
		Thread thread = new Thread(pir);
		thread.start();
		
		return pir;
	}
	
	@Override
	public void run() {
		passedTime = 0;
		for(;;) {
			passedTime += SLEEPING_TIME;
			if ((passedTime - TIME_TO_TURN_OFF) == 0) {
				System.out.println("Turning off screen");
				logger.debug("Turning off screen");
				display.off();
				passedTime = 0;
			}
			try {
				Thread.sleep(SLEEPING_TIME);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}
	
//		screen_on = True
//				timeout = 60
//				detected_person_at = time()
//				fbset_command_hack = ["/bin/fbset -xres 1184 -yres 928 -depth 16", "/bin/fbset -xres 1920 -yres 1080 -depth 16"]
//				fucking_python_no_increment_operator = 0
//
//				while True:
//
//					person_detected = GPIO.input(7)
//
//				    if person_detected == True:
//				        print "Person detected"
//				        detected_person_at = time()
//
//				        if screen_on == False:
//				            print "Turning ON the lcd"
//				            screen_on = True
//				            os.system("/usr/bin/tvservice -p")
//				            fucking_python_no_increment_operator = (fucking_python_no_increment_operator + 1) % 2
//				            os.system(fbset_command_hack[fucking_python_no_increment_operator])
//					
//				    elif (time() - detected_person_at) >= timeout and screen_on == True:
//						print "turning off the LCD"
//				        screen_on = False
//				        os.system("/usr/bin/tvservice -o")
//
//					timer.sleep(1)

//		GpioPinDigitalInput myButton = gpio.provisionDigitalInputPin(RaspiPin.GPIO_02,             // PIN NUMBER
//                "MyButton",

	
	public static void main(String[] args) throws InterruptedException {
		PIRSensor s = PIRSensor.getInstance(new MyDisplay());
	}
}

class MyDisplay implements PhotosDisplay {
	public MyDisplay() {
	}

	public void on() {
		System.out.println("ON: called");
	}
	
	public void off() {
		System.out.println("OFF: called");
	}

	@Override
	public void showPhoto(File photo) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void redisplayCurrentPhoto() {
		// TODO Auto-generated method stub
		
	}
}
