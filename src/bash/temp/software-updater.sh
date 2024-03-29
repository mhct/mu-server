#!/bin/bash

# 09/08/2013
# @mariohct
#
# Software updater for the mu-server software
#

UPDATE_URL=http://www.missingyouframe.com/mu-updates/update.txt
BASE_FOLDER=/Users/danirigolin/Documents/projetos/mu-server/src/bash/temp
SERVER_PID=$(cat $BASE_FOLDER/pid)

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


function download_apply_update {
    update_url=$1
    update_sha=$2

    echo "Downloading updates"
    $(curl $update_url -o /tmp/mu-server-update.tar.gz)
   
    # 
    # compares downloaded file SHA with the one described at the update URL
    #
    if [ "$update_sha" != "$(shasum /tmp/mu-server-update.tar.gz|awk '{print $1}')" ]
    then
            echo "Problem with the downloaded file. SHASUM is different."
    else
            echo "Applying updates"
            cd 
            $(tar zxvf /tmp/mu-server-update.tar.gz -C $BASE_FOLDER/)

            #
            # backsup previous simlink and links to new version
            #
            echo "Symlinking new version"
            version_name=$(tar tvf /tmp/mu-server-update.tar.gz |awk -F/ '{if (NF<3) print}'|awk '{print $(NF-0)}')
            rm $BASE_FOLDER/mu-server_previous
            mv $BASE_FOLDER/mu-server $BASE_FOLDER/mu-server_previous
            ln -s $BASE_FOLDER/$version_name $BASE_FOLDER/mu-server

            touch_version_file $update_sha
    fi
}

function touch_version_file {
    echo "Updating current_version.txt to: " $1
    echo "$1" > $BASE_FOLDER/current_version.txt
}

#
# TODO improve error checking, for loading the new software... perhaps it could invoke some software
# functions to guarantee the new version is loaded and working
#
function restart_mu_server {
    echo "Restarting current software: " $SERVER_PID
    $(kill -s 9 $SERVER_PID)
}

update_url=""
update_sha=""

cd $BASE_FOLDER
pwd
fetch_update_data update_url update_sha
#touch_version_file $update_sha

#echo $update_sha
#echo $(cat ./current_version.txt)

if [ "$update_sha" != "$(cat ./current_version.txt)" ]
then
    download_apply_update $update_url $update_sha
    #touch_version_file $update_sha
    restart_mu_server
    pwd
else
    echo "Already latest version of the software"    
fi

