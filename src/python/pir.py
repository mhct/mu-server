#
# First tests with the RaspberryPi GPIO
#
# 24/03/2013
# @mariohct
# 
import RPi.GPIO as GPIO
import time as timer
import os
from time import time

GPIO.setmode(GPIO.BCM)
GPIO.setup(7, GPIO.IN)

screen_on = True
timeout = 60
detected_person_at = time()
fbset_command_hack = ["/bin/fbset -xres 1184 -yres 928 -depth 16", "/bin/fbset -xres 1920 -yres 1080 -depth 16"]
fucking_python_no_increment_operator = 0

while True:

	person_detected = GPIO.input(7)

	if person_detected == True:
	    print "Person detected"
	    detected_person_at = time()
	
	    if screen_on == False:
	        print "Turning ON the lcd"
	        screen_on = True
	        os.system("/usr/bin/tvservice -p")
	        fucking_python_no_increment_operator = (fucking_python_no_increment_operator + 1) % 2
	        os.system(fbset_command_hack[fucking_python_no_increment_operator])
	
	elif (time() - detected_person_at) >= timeout and screen_on == True:
		print "turning off the LCD"
		screen_on = False
		os.system("/usr/bin/tvservice -o")

	timer.sleep(1)
