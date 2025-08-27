#!/bin/sh
cd infra/containers/ || exit 1

#! init database
cd database/ || exit 1
./db_build.sh
./db_run.sh

#! init kafka
cd ..
cd kafka/ || exit 1
./kafka_build.sh
./kafka_run.sh
