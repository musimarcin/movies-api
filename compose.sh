#!/bin/bash

declare dc_db=docker-compose.yml
declare dc_api=docker-compose-api.yml

function build_api() {
    mvn clean install -DskipTests
}

function start() {
  build_api
  echo "Starting docker containers"
  docker-compose -f ${dc_db} -f ${dc_api} up --build -d
  docker-compose -f ${dc_db} -f ${dc_api} logs -f
}

function stop() {
    echo "Stopping all containers"
    docker-compose -f ${dc_db} -f ${dc_api} stop
    docker-compose -f ${dc_db} -f ${dc_api} rm -f
}

function restart() {
    stop
    sleep 5
    start
}

action="start"

#if there is argument assign to action that argument
if [ "$#" != "0" ]
then
  action=$*
fi

eval "${action}"