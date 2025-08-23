#!/bin/sh

# stop running service if any
mTargetId=$(docker ps | grep thingsbe-db | awk -F thingsbe-db '{print $1}')
if [ -n "$mTargetId" ]; then
    echo "Stopping service $mTargetId"
    docker stop "$mTargetId"
    docker rm "$mTargetId" 2>/dev/null
else
    echo "No running thingsbe-db service found"
fi

# also stop by container name if exists
docker stop thingsbe-database 2>/dev/null
docker rm thingsbe-database 2>/dev/null
