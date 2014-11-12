#!/bin/bash
#
# MU-Server linux preparation tool
#
# The goal of this tool is to facilitate creating new Mu-Server images
#
# 02/09/2013
# @mariohct
#

CONFIG_DIR=/root/tools

pacman -Sy

pacman -S ozerocdoff networkmanager xorg-xinit xterm xorg-server xf86-video-fbdev \
xorg-xset libxtst mesa xf86-video-fbdev xf86-video-vesa xorg-server-utils fbset vim avahi xdotool dkms nodejs \
linux-headers-raspberrypi-latest linux-raspberrypi-latest graphicsmagick jasper libpng libwebp librsvg \
xorg-xwd


mkdir -p /home/pi
mkdir -p /home/pi/raw_photos_folder
mkdir -p /home/pi/photos_folder

#
# Adjusts timezone to Brussels
#
timedatectl set-timezone Europe/Brussels

#
# Installing Oracle JVM
#
curl http://www.missingyouframe.com/mu-server-downloads/java.tar.gz -o /root/java.tar.gz
tar zxvf /root/java.tar.gz -C /home/pi


#
# Installing mu-server tools
#
curl http://www.missingyouframe.com/mu-server-downloads/tools.tar.gz -o /root/tools.tar.gz
tar zxvf /root/tools.tar.gz -C /root


#
# Adding auto-login
#
mkdir -p /etc/systemd/system/getty@tty1.service.d/
cp $CONFIG_DIR/autologin.conf /etc/systemd/system/getty@tty1.service.d/
cp $CONFIG_DIR/bash_profile /root/.bash_profile


#
# Configures HSO module for 3G modem
#
cp $CONFIG_DIR/ozerocdoff.service /usr/lib/systemd/system

tar zxpvf $CONFIG_DIR/hso-1.9.tar.gz -C /usr/src
dkms add -m hso -v 1.9
dkms build -m hso -v 1.9
dkms install  -m hso -v 1.9


#
# Creates configuration for modem and MobileVikings, BASE
#
cp $CONFIG_DIR/mobile_vikings /etc/NetworkManager/system-connections
cp $CONFIG_DIR/base /etc/NetworkManager/system-connections
chown root:root /etc/NetworkManager/system-connections/mobile_vikings
chmod 600 /etc/NetworkManager/system-connections/mobile_vikings

chown root:root /etc/NetworkManager/system-connections/base 
chmod 600 /etc/NetworkManager/system-connections/base

#
# Installs MU-server soft 
#
cp $CONFIG_DIR/software-updater.sh /home/pi
cp $CONFIG_DIR/application.conf /home/pi
chmod 755 /home/pi/software-updater.sh
/home/pi/software-updater.sh

#
# Config Swap space
# info at https://wiki.archlinux.org/index.php/Swap
#
#/usr/bin/dd if=/dev/zero of=/swapfile bs=1024 count=131072
#chmod 600 /swapfile
#mkswap /swapfile
#swapon /swapfile
#echo /swapfile none swap defaults 0 0 >> /etc/fstab

#
# Installs crontab
#
/usr/bin/crontab $CONFIG_DIR/cron

#
# Adjusts /boot/config
#
echo # Mu-server configs >> /boot/config.txt
echo disable_overscan=1 >> /boot/config.txt
echo display_rotate=2 >> /boot/config.txt


#
# Activate system services
#
systemctl enable NetworkManager
systemctl enable ModemManager
systemctl enable dkms.service
systemctl enable multi-user.target
systemctl enable ozerocdoff

