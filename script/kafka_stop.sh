#!/bin/sh

# stop running service if any
mTargetId=$(docker ps | grep sentientgrid-kafka | awk -F sentientgrid-kafka '{print $1}')
if [ -n "$mTargetId" ]; then
    echo "Stoping service $mTargetId"
    docker stop "$mTargetId"
fi