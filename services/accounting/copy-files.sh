#!/bin/bash

sleep 2

java -jar app.jar --spring.profiles.active=prod --server.port=8443 --server.ssl.enabled=false