#!/bin/sh
export PATH="script/:$PATH"

#! init database
db_build.sh
db_run.sh

#! init kafka
kafka_build.sh
kafka_run.sh
