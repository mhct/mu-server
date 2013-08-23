#!/bin/bash
#
# Loads the X-server with the script to load the mu-server
# 

#BASE
mcli con up uuid bff144b0-feb2-11e2-b778-0800200c9a66

#MOBILE_VIKINGS
#nmcli con up uuid cb2571c5-9899-40a6-bfe4-385993b7799b 

startx /home/pi/mu-server/bin/run_muserver.sh




