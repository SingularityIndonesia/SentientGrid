#!/bin/sh
# build docker image
kafka_build.sh
#clear

# stop running service if any
kafka_stop.sh
#clear

# start new service
docker run -d -p 9092:9092 -p 9093:9093 --name sentientgrid-kafka sentientgrid-kafka
#clear