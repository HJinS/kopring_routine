#!/usr/bin/env bash

docker-compose -f docker_builds/DockerCompose.yml down --remove-orphans
docker rmi routine_nginx routine
docker-compose -f docker_builds/DockerCompose.yml up -d --build