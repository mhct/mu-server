#!/bin/bash
#
# Loads the mu-server software
# @mariohct
# 21/07/2013
#

/usr/bin/xset s off
/usr/bin/xset -dpms
/usr/bin/xset s noblank

BASE_FOLDER=/home/pi
CONFIG_FILE=$BASE_FOLDER/application.conf
LOGS_FILE=$BASE_FOLDER/mu-server/logs.txt 

export CLASSPATH=/home/pi/mu-server/lib/*
export PHOTOS_FOLDER="/home/pi/show_photos/"
export DATABASE_FOLDER="/home/pi/MuServerDB"

if [ ! -d $PHOTOS_FOLDER ]; then
        mkdir -p $PHOTOS_FOLDER
fi


#
# Checks if it should turn on the 3G Modem
#
connection=$(grep connection-type $CONFIG_FILE |cut -d= -f2|sed s/[\"\ ]//g)
if [ "3gmodem" == "$connection" ]
then
    /usr/bin/nmcli con up uuid bff144b0-feb2-11e2-b778-0800200c9a66
	sleep 10		
fi


/home/pi/jdk1.8.0/bin/java -Dconfig.file=$CONFIG_FILE com.muframe.server.MuServer >> $LOGS_FILE 2>&1
echo $! > $BASE_FOLDER/pid
