#!/bin/bash

#
# MU-Controller
#
# loads the imapclient
# starts the slideshow
# 
# TODO add inotify tools 
# http://linux.die.net/man/1/inotifywait
#
LOG_FILE="./mulog.txt"

export PHOTOS_FOLDER="/home/pi/mu-server/show_photos/"

function log {
	echo $(date) $1 >> $LOG_FILE
}

startx /home/pi/mu-server/bin/run_muserver.sh

#while RES=$(inotifywait -e create $PHOTOS_FOLDER);
#do
#	log "checking new pictures"
#	file=${RES#?*CREATE }
#	log "filename: "$file
#	sleep 45
#	killall fbi
#	fbi -T 1 -noverbose -m 1920x1080 -a $PHOTOS_FOLDER/$file &
#	log "showing new picture"	
#done




