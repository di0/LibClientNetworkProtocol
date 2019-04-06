#!/bin/sh

rm -R bin
mkdir bin

javac -d bin -cp .:lib/* SshOrTelnet.java

if [ "$#" -ne 1 ]
then
	java -Ddebug="true" -cp .:lib/*:bin SshOrTelnet
else
	java -Ddebug="$1" -cp .:lib/*:bin SshOrTelnet
fi


