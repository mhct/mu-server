#!/bin/bash

#
# MU-Controller
#
# loads the imapclient
# starts the slideshow
# 
# TODO add inotify tools 
# http://linux.die.net/man/1/inotifywait
# allow entering the smtp server. now it is hardcoded on the python script
#
# secret.txt in the format
# email_user@gmail.com:password
#
LOG_FILE="./mulog.txt"
export EMAIL_ACCOUNT=$(cut -f 1 -d : secret.txt)
export EMAIL_PASSWORD=$(cut -f 2 -d : secret.txt)
export PHOTOS_DIR="/home/pi/mu-photos/raw_photos"

SHOW_PHOTOS_DIR="/home/pi/mu-photos/show_photos"
INTERVAL=120


function slideshow {
	fbi -T 1 -noverbose -m 1920x1080 -a -t $(($INTERVAL/$(ls -1 $SHOW_PHOTOS_DIR/*|wc -l))) $SHOW_PHOTOS_DIR/* &
	#fim -q $SHOW_PHOTOS_DIR/* &
	log "Starting new slideshow"
}

function log {
	echo $(date) $1 >> $LOG_FILE
}

while :
do
	log "checking new pictures"
	python imap_client.py
	temp=$(ls -1 $PHOTOS_DIR | wc -l)
	if [ $temp -gt 0 ];then
		#rm $SHOW_PHOTOS_DIR/*
		mv $PHOTOS_DIR/* $SHOW_PHOTOS_DIR/
		killall fbi 
		slideshow
	fi
	sleep $INTERVAL
done


