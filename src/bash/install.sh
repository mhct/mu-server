#!/bin/bash

#
# Installation script for the mu-server
#
# 21/03/2013
# @mhct
#

#
# Fetching dependencies
#
sudo apt-get update

#
# Mu-Server python + FBI
#
#sudo apt-get install vim fim fbi netatalk openjdk-7-jdk inotify-tools python-setuptools
#sudo easy_install pip xterm x11-xserver-utils

#
# Mu-server 0.0.1
#
sudo apt-get install vim netatalk xterm x11-xserver-utils

#
# Install jdk 1.8
#
Copy from our repository... to /home/pi

#
# Configuring WIFI
#
#WIFI_NETWORK="FamiliaTrapo"
#WIFI_PASSWORD="password"

#sudo wpa_password $WIFI_NETWORK $WIFI_PASSWORD >> /etc/wpa_supplicant/wpa_supplicant.conf

#sudo reboot
 
mkdir -p /home/pi/mu-server
mkdir -p /home/pi/mu-server/bin /home/pi/mu-server/show_photos/

wget http://pi4j.googlecode.com/files/pi4j-0.0.5.deb
sudo dpkg -i pi4j-0.0.5.deb


#
# prepare second user
#
adduser mhct
mkdir /home/mhct/.ssh
copy id_rsa.pub key to /home/mhct/.ssh/authorized_keys

#
# Check readme file for more files to change.. 
# TODO add the other changes here too
#
