#!/bin/bash

#
# Creates a printscreen and uploads to the cloud backend
#

SERVER=http://app.missyouframe.com:9090
BASE_FOLDER=/home/pi
LOGS_FILE=$BASE_FOLDER/mu-server/logs.txt
devicename=$(cat /home/pi/devicename.txt)
filename=/tmp/printscreen.xwd

/usr/bin/xwd -display :0.0 -root -out $filename
echo -n $(date) >> $LOGS_FILE
echo " Uploading printscreen to cloud" >> $LOGS_FILE
/usr/bin/curl -X POST -F "file=@$filename" $SERVER/$devicename/photos  >> $LOGS_FILE 2>&1 

