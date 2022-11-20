#!/usr/bin/env bash

docker-compose -f docker_builds/DockerCompose.yml down --remove-orphans
docker rmi docker_builds_routine_nginx docker_builds_routine_redis docker_builds_routine docker_builds_routine_db
docker-compose -f docker_builds/DockerCompose.yml up -d