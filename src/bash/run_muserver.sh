/usr/bin/xset s off
/usr/bin/xset -dpms
/usr/bin/xset s noblank

BASE_FOLDER=/home/pi

/home/pi/jdk1.8.0/bin/java -cp $BASE_FOLDER/mu-server/bin/:$BASE_FOLDER/mu-server/bin/mu-server.jar com.muframe.server.MuServer >> $BASE_FOLDER/mu-server/logs.txt &
echo $! > $BASE_FOLDER/pid

#/usr/bin/java -cp /home/pi/muserver.jar com.muframe.server.MuServer

