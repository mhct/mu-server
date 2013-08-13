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

    echo "Checking for updates"
    update_data=$(curl $UPDATE_URL)
    
    local url=$(echo "$update_data"|cut -d, -f 1)
    local sha=$(echo "$update_data"| cut -d, -f 2) 
    #echo $url " - SHA:" $sha 

    eval $__update_url="'$url'"
    eval $__update_sha="'$sha'"
    
}


function download_update {
    update_url=$1
    update_sha=$2

    echo "Downloading updates"
    $(curl $update_url -o /tmp/mu-server-update.tar.gz)
    # do the checksum
    #$(shasum /tmp/mu-server-update.tar.gz
    echo "Applying updates"
    $(tar zxvf /tmp/mu-server-update.tar.gz -C ./)
}

function touch_version_file {
    echo "$1" > ./current_version.txt
}

update_url=""
update_sha=""

fetch_update_data update_url update_sha
touch_version_file $update_sha

if [ "$update_sha" != $(cat ./current_version.txt) ]
then
    download_update $update_url $update_sha
else
    echo "Already latest version of the software"    
fi




