#!/bin/bash

#get current directory
current_dir=$(pwd)

#get parent directory
parent_dir="$(dirname "$current_dir")"

#create jar file
mvn package -f "$parent_dir"/pom.xml

#move jar file to binaries
mv "$parent_dir"/target/ser516-group10-biscuit-1.0-SNAPSHOT-jar-with-dependencies.jar ser516-group10-biscuit.jar

#remove target directory
mvn clean -f "$parent_dir"/pom.xml