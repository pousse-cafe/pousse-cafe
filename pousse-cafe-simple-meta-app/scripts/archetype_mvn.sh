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
echo "	Adding parent element"
xmlstarlet ed -L -N "n=http://maven.apache.org/POM/4.0.0" -i "/n:project/n:groupId" -t elem -n parent -v "" pom.xml
xmlstarlet ed -L -N "n=http://maven.apache.org/POM/4.0.0" -s "/n:project/n:parent" -t elem -n "groupId" -v "org.pousse-cafe-framework" pom.xml
xmlstarlet ed -L -N "n=http://maven.apache.org/POM/4.0.0" -s "/n:project/n:parent" -t elem -n "artifactId" -v "pousse-cafe" pom.xml
xmlstarlet ed -L -N "n=http://maven.apache.org/POM/4.0.0" -s "/n:project/n:parent" -t elem -n "version" -v "$VERSION" pom.xml
echo "	Remove elements conflicting with/overriding parent"
xmlstarlet ed -L -N "n=http://maven.apache.org/POM/4.0.0" -d "/n:project/n:groupId" pom.xml
xmlstarlet ed -L -N "n=http://maven.apache.org/POM/4.0.0" -d "/n:project/n:version" pom.xml
xmlstarlet ed -L -N "n=http://maven.apache.org/POM/4.0.0" -d "/n:project/n:developers" pom.xml
xmlstarlet ed -L -N "n=http://maven.apache.org/POM/4.0.0" -d "/n:project/n:licenses" pom.xml
xmlstarlet ed -L -N "n=http://maven.apache.org/POM/4.0.0" -d "/n:project/n:scm" pom.xml
xmlstarlet ed -L -N "n=http://maven.apache.org/POM/4.0.0" -d "/n:project/n:distributionManagement" pom.xml
echo "	Updating description"
xmlstarlet ed -L -N "n=http://maven.apache.org/POM/4.0.0" -u "/n:project/n:description" -v "$DESCRIPTION" pom.xml

echo "Archetype resource POM fixing..."
TEMPLATE_POM=src/main/resources/archetype-resources/pom.xml
echo "	Fixing dependencies version"
sed -i s/\$\{project.version\}/$VERSION/g $TEMPLATE_POM
echo "	Removing useless elements"
xmlstarlet ed -L -N "n=http://maven.apache.org/POM/4.0.0" -d "/n:project/n:url" $TEMPLATE_POM
xmlstarlet ed -L -N "n=http://maven.apache.org/POM/4.0.0" -d "/n:project/n:build" $TEMPLATE_POM

mvn "$@"
