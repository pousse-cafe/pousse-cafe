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

mvn archetype:create-from-project -Darchetype.properties=archetype.properties -Darchetype.keepParent=true

cd target/generated-sources/archetype/

xmlstarlet ed -L -N "n=http://maven.apache.org/POM/4.0.0" -u "/n:project/n:description" -v "$DESCRIPTION" pom.xml
xmlstarlet ed -L -N "n=http://maven.apache.org/POM/4.0.0" -i "/n:project/n:build" -t elem -n properties pom.xml
xmlstarlet ed -L -N "n=http://maven.apache.org/POM/4.0.0" -s "/n:project/n:properties" -t elem -n "project.build.sourceEncoding" -v "UTF-8" pom.xml

TEMPLATE_POM=src/main/resources/archetype-resources/pom.xml
sed -i s/\$\{project.version\}/$VERSION/g $TEMPLATE_POM
xmlstarlet ed -L -N "n=http://maven.apache.org/POM/4.0.0" -d "/n:project/n:url" $TEMPLATE_POM
xmlstarlet ed -L -N "n=http://maven.apache.org/POM/4.0.0" -d "/n:project/n:build" $TEMPLATE_POM

mvn "$@"
