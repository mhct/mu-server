#!/bin/bash

#
# MU-Controller
#
# loads the imapclient
# starts the slideshow
#
export EMAIL_ACCOUNT="danirigolin@gmail.com"
export EMAIL_PASSWORD="aruska211"
export PHOTOS_DIR="/home/pi/mu-photos/raw_photos"

SHOW_PHOTOS_DIR="/home/pi/mu-photos/show_photos"
INTERVAL=20

function slideshow {
	fbi -noverbose -m 1920x1080 -a -t $INTERVAL $SHOW_PHOTOS_DIR/*
}
 
while :
do
	python imap_client.py
	temp=$(ls -1 $PHOTOS_DIR | wc -l)
	if [ $temp -gt 0 ];then
		mv $PHOTOS_DIR/* $SHOW_PHOTOS_DIR/
		killall fbi
		slideshow
	fi
	sleep 60
done
