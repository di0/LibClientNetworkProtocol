#!/bin/sh

rm -R bin
mkdir bin

javac -d bin -cp .:lib/* ScpTest.java

if [ "$#" -ne 1 ]
then
	java -Ddebug="true" -cp .:lib/*:bin ScpTest
else
	java -Ddebug="$1" -cp .:lib/*:bin ScpTest
fi


