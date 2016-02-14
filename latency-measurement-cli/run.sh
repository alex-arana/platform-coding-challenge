#!/bin/sh
cd $(dirname $0)

mvn clean package spring-boot:run -Drun.arguments="$@"
exit
