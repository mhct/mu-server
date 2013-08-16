/usr/bin/xset s off
/usr/bin/xset -dpms
/usr/bin/xset s noblank


/home/pi/jdk1.8.0/bin/java -cp /home/pi/mu-server/bin/:/home/pi/mu-server/bin/mu-server.jar com.muframe.server.MuServer >> /home/pi/mu-server/logs.txt
#/usr/bin/java -cp /home/pi/muserver.jar com.muframe.server.MuServer

