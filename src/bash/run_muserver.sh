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
export IM4JAVA_TOOLPATH="/usr/bin"

if [ ! -d $PHOTOS_FOLDER ]; then
        mkdir -p $PHOTOS_FOLDER
fi


#
# Checks if it should turn on the 3G Modem
#
function checkModem {
        ISIT=$(/sbin/ifconfig hso0 | grep "UP" | wc -l)

        if test $ISIT -gt 0;
        then
                echo "up";
        else
                echo "down";
        fi
}

connection=$(grep connection-type $CONFIG_FILE |cut -d= -f2|sed s/[\"\ ]//g)
if [ "3gmodem" == "$connection" ]
then
    connected=$(checkModem)
    while [ "$connected" == "down" ]; do
        echo "trying to connect using Modem" >> $LOGS_FILE
        /usr/bin/nmcli con up uuid bff144b0-feb2-11e2-b778-0800200c9a66
        connected=$(checkModem)
	sleep 10
    done

    echo "Connected using Modem" >> $LOGS_FILE
fi

ssh -o ServerAliveInterval 180 $PORT:127.0.0.1:22 mario@ssh.cs.kuleuven.be -N &

/home/pi/jdk1.8.0/bin/java -Dconfig.file=$CONFIG_FILE com.muframe.server.MuServer >> $LOGS_FILE 2>&1

