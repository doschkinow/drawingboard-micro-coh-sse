#!/bin/bash -x
# start a drawingboard microservice, based on coherence
#

jarfile=$(ls /pipeline/source/target/*with-dependencies.jar)
options="-Dcoherence.cacheconfig=drawingboard-coherence-cache-config.xml"
wka_options="-Dcoherence.wka=cache"

ping -c 1 -W 1 cache
if [ $? -ne 0 ]
then
    if [ $1 == 'sse' ] || [ $1 == 'ws' ]
    then
        echo "exiting since no cache service available..."
        exit 1
    fi
fi


case $1 in
sse)    java $options $wka_options \
            -Dcoherence.distributed.localstorage=false  \
            -Dcoherence.jcache.configuration.classname=passthrough \
            -jar $jarfile;;
ws)     java $options $wka_options \
            -Dcoherence.distributed.localstorage=false  \
            -Dcoherence.jcache.configuration.classname=passthrough \
            -jar $jarfile;;
cache)  java $options $wka_options \
            -jar $jarfile;;
*)      echo error: usage: $0 "cache|ws|sse";;
esac