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
Pousse-Café is a framework assisting in writing DDD-based applications. This is the archetype for a simple app.
_EOF_

mvn archetype:create-from-project -Darchetype.properties=archetype.properties -Darchetype.keepParent=false

cd target/generated-sources/archetype/

echo "Archetype POM fixing..."
ARCHETYPE_POM=pom.xml

echo "  Adding parent element"
xmlstarlet ed -L -N "n=http://maven.apache.org/POM/4.0.0" -i "/n:project/n:groupId" -t elem -n parent -v "" $ARCHETYPE_POM
xmlstarlet ed -L -N "n=http://maven.apache.org/POM/4.0.0" -s "/n:project/n:parent" -t elem -n "groupId" -v "org.pousse-cafe-framework" $ARCHETYPE_POM
xmlstarlet ed -L -N "n=http://maven.apache.org/POM/4.0.0" -s "/n:project/n:parent" -t elem -n "artifactId" -v "pousse-cafe" $ARCHETYPE_POM
xmlstarlet ed -L -N "n=http://maven.apache.org/POM/4.0.0" -s "/n:project/n:parent" -t elem -n "version" -v "$VERSION" $ARCHETYPE_POM

echo "    Removing useless elements"
xmlstarlet ed -L -N "n=http://maven.apache.org/POM/4.0.0" -d "/n:project/n:version" $ARCHETYPE_POM

echo "    Updating description"
xmlstarlet ed -L -N "n=http://maven.apache.org/POM/4.0.0" -u "/n:project/n:description" -v "$DESCRIPTION" $ARCHETYPE_POM

echo "    Adding properties element"
xmlstarlet ed -L -N "n=http://maven.apache.org/POM/4.0.0" -i "/n:project/n:build" -t elem -n properties -v "" $ARCHETYPE_POM
xmlstarlet ed -L -N "n=http://maven.apache.org/POM/4.0.0" -s "/n:project/n:properties" -t elem -n "project.build.sourceEncoding" -v "UTF-8" $ARCHETYPE_POM


echo "Archetype resource POM fixing..."
TEMPLATE_POM=src/main/resources/archetype-resources/pom.xml

echo "    Fixing dependencies version"
sed -i s/\$\{project.version\}/$VERSION/g $TEMPLATE_POM

echo "    Removing useless elements"
xmlstarlet ed -L -N "n=http://maven.apache.org/POM/4.0.0" -d "/n:project/n:url" $TEMPLATE_POM

mvn "$@"
