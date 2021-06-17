#!/bin/sh

#
#    Set 'true' parameter after command to turn on debug. eg:
#
#   ./runSshOrTelnetDemo.sh true
####

rm -R bin
mkdir bin

javac -d bin -cp .:lib/* SshOrTelnet.java

if [ "$#" -ne 1 ]
then
	java -Ddebug="false" -cp .:lib/*:bin SshOrTelnet
else
	java -Ddebug="$1" -cp .:lib/*:bin SshOrTelnet
fi


