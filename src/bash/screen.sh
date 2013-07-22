#!/bin/bash

if [ "$1" == 'on' ]; then
  #tvservice -p;
  #fbset -depth 8;
  #fbset -depth 16;
  #chvt 6;
  #chvt 7;
  echo 'Switched Screen ON!'
  /usr/bin/xdotool key a
  
  /usr/bin/xset s off
  /usr/bin/xset -dpms
  /usr/bin/xset s noblank
fi

if [ "$1" == 'off' ]; then
  #tvservice o
  /usr/bin/xset dpms force off
  echo 'Switched Screen OFF!'
fi
