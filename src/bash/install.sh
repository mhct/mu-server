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
sudo apt-get install fbi netatalk openjdk-7-jdk inotify-tools


#
# Configuring WIFI
#
WIFI_NETWORK="FamiliaTrapo"
WIFI_PASSWORD="password"

sudo wpa_password $WIFI_NETWORK $WIFI_PASSWORD >> /etc/wpa_supplicant/wpa_supplicant.conf

sudo reboot
 
