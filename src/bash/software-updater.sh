#!/bin/bash

# 09/08/2013
# @mariohct
#
# Software updater for the mu-server software
#

UPDATE_URL=http://www.missingyouframe.com/mu-updates/update.txt


function fetch_update_data {
    local __update_url=$1
    local __update_sha=$2

    update_data=$(curl $UPDATE_URL)
    
    update_url=$(cut -d, -f 1 $update_data)
    update_sha=$(cut -d, -f 1 $update_data)
    
    eval $__update_url="'$update_url'"
    eval $__update_sha="'$update_sha'"
    
}


function download_update {
    update_url=$1
    update_sha=$2

    $(curl $update_url -o /tmp/mu-server-update.tar.gz)
    # do the checksum
    #$(shasum /tmp/mu-server-update.tar.gz
    $(tar zxvf /tmp/mu-server-update.tar.gz -C /home/pi/mu-server)
}

server_url=$(check_update_availability)




