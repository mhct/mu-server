package com.muframe.server;

import com.pi4j.io.gpio.Gpio;
import com.pi4j.io.gpio.GpioFactory;
import com.pi4j.io.gpio.GpioPin;
import com.pi4j.io.gpio.Pin;
import com.pi4j.io.gpio.event.GpioListener;
import com.pi4j.io.gpio.event.GpioPinStateChangeEvent;
import com.pi4j.io.gpio.impl.GpioControllerImpl;

public class PIRSensor {
	public PIRSensor() {
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

	}
	
	public static void main(String[] args) throws InterruptedException {
		final Gpio gpio = GpioFactory.createInstance();
		GpioPin myButton = gpio.provisionInputPin(Pin.GPIO_06, "pin7");
		myButton.addListener(new GpioListener() {
			
			@Override
			public void pinStateChanged(GpioPinStateChangeEvent event) {
				System.out.println(" ---> GPIO state changed " + event.getPin() + ", state: " + event.getState());
			}
		});
		
		for(;;) {
			Thread.sleep(1000);
		}
	}
}
