#!/bin/bash

VERSION=$(xmlstarlet sel -N "n=http://maven.apache.org/POM/4.0.0" -t -v "/n:project/n:version" pom.xml)
if [[ $VERSION == *-SNAPSHOT ]]
then
	exit 0
else
	exit 1
fi
