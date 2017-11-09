#!/bin/bash -x
# start a drawingboard microservice, based on coherence
#
# if started from a swarm stack using its name as a second parameter, all services
# are prefixed with the stack name
if [ $# == 2 ]
then
    svc_prefix=$2_
else
    svc_prefix=""
fi


# get the ip address of the local overlay network interface
# the "grep 24" a hack to discard the ip for the service, which has a mask of 32 and on some 
# systems appear on interface lo, on other on eth0
subnet=12.0.0.[0-9]+
localip=`ip a |grep 24 | grep eth0 | egrep -o -e $subnet`

jarfile=$(ls /pipeline/source/target/*with-dependencies.jar)
options="-Dcoherence.cacheconfig=drawingboard-coherence-cache-config.xml -Dcoherence.localhost=$localip"
wka_options="-Dcoherence.wka=tasks.${svc_prefix}cache"

ping -c 1 -W 1 tasks.${svc_prefix}cache
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
*)      echo error: usage: $0 "cache|ws|sse" "optional-swarm-stack-name";;
esac