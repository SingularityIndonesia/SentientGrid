#!/bin/sh
# build docker image
db_build.sh
#clear

# stop running service if any
db_stop.sh
#clear

# start new service
echo "Starting PostgreSQL database service..."
docker run -d -p 9094:9094 --name sentientgrid-db sentientgrid-db
#clear

# show running containers
echo "Database service started!"
docker ps | grep sentientgrid-db

# show connection info
echo ""
echo "PostgreSQL Database Connection Info:"
echo "======================================"
echo "Host: localhost"
echo "Port: 9094"
echo "Database: thingsdb"
echo "User: thingsuser"
echo "Password: thingspass"
echo ""
echo "To connect using psql:"
echo "psql -h localhost -p 9094 -U thingsuser -d thingsdb"
