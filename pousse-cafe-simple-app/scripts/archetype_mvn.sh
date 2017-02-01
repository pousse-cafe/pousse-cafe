#!/bin/bash

set -e

if [[ $# < 1 ]]
then
	echo "No goal provided"
	exit 1
fi

VERSION=$(xmlstarlet sel -N "n=http://maven.apache.org/POM/4.0.0" -t -v "/n:project/n:parent/n:version" pom.xml)

echo "Detected version: $VERSION"

read DESCRIPTION <<-"_EOF_"
Pousse-Café is a framework assisting in writing DDD-based applications. This is a simple meta-app archetype.
_EOF_

mvn archetype:create-from-project -Darchetype.properties=archetype.properties -Darchetype.keepParent=true

cd target/generated-sources/archetype/

echo "Archetype POM fixing..."
echo "	Updating description"
xmlstarlet ed -L -N "n=http://maven.apache.org/POM/4.0.0" -u "/n:project/n:description" -v "$DESCRIPTION" pom.xml
echo "	Adding properties empty element"
xmlstarlet ed -L -N "n=http://maven.apache.org/POM/4.0.0" -i "/n:project/n:build" -t elem -n properties -v "" pom.xml
echo "	Adding source encoding"
xmlstarlet ed -L -N "n=http://maven.apache.org/POM/4.0.0" -s "/n:project/n:properties" -t elem -n "project.build.sourceEncoding" -v "UTF-8" pom.xml

echo "Archetype resource POM fixing..."
TEMPLATE_POM=src/main/resources/archetype-resources/pom.xml
echo "	Fixing dependencies version"
sed -i s/\$\{project.version\}/$VERSION/g $TEMPLATE_POM
echo "	Removing URL"
xmlstarlet ed -L -N "n=http://maven.apache.org/POM/4.0.0" -d "/n:project/n:url" $TEMPLATE_POM
echo "	Removing build"
xmlstarlet ed -L -N "n=http://maven.apache.org/POM/4.0.0" -d "/n:project/n:build" $TEMPLATE_POM

mvn "$@"
