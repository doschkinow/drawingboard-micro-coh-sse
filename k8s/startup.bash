#!/bin/bash -x
# start a drawingboard microservice, based on coherence
#

jarfile=$(ls /pipeline/source/target/*with-dependencies.jar)
options="-Dcoherence.cacheconfig=drawingboard-coherence-cache-config.xml -Dcoherence.cluster=drawingboard \
         -Dcoherence.distributed.localstorage=false -Dcoherence.jcache.configuration.classname=passthrough"
wka_options="-Dcoherence.wka=drawingboard-cache"

ping -c 1 -W 1 drawingboard-cache
if [ $? -ne 0 ]
then
        echo "exiting since no cache service available..."
        exit 1
fi

java $options $wka_options -jar $jarfile
