#!/bin/bash

set -e

if [[ $# < 1 ]]
then
	echo "No goal provided"
	exit 1
fi

VERSION=$(xmlstarlet sel -N "n=http://maven.apache.org/POM/4.0.0" -t -v "/n:project/n:parent/n:version" pom.xml)

read DESCRIPTION <<-"_EOF_"
Pousse-Café is a framework assisting in writing DDD-based applications. This is a simple meta-app archetype.
_EOF_

mvn archetype:create-from-project -Darchetype.properties=archetype.properties

cd target/generated-sources/archetype/

xmlstarlet ed -L -N "n=http://maven.apache.org/POM/4.0.0" -u "/n:project/n:description" -v "$DESCRIPTION" pom.xml

sed -i s/\$\{project.version\}/$VERSION/g src/main/resources/archetype-resources/pom.xml

mvn "$@"
