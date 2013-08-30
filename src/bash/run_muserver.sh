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

/home/pi/jdk1.8.0/bin/java -Dconfig.file=$CONFIG_FILE com.muframe.server.MuServer >> $LOGS_FILE
echo $! > $BASE_FOLDER/pid
