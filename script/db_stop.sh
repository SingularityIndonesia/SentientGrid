#!/bin/sh

# stop running service if any
mTargetId=$(docker ps | grep sentientgrid-db | awk -F sentientgrid-db '{print $1}')
if [ -n "$mTargetId" ]; then
    echo "Stopping service $mTargetId"
    docker stop "$mTargetId"
    docker rm "$mTargetId" 2>/dev/null
else
    echo "No running sentientgrid-db service found"
fi
