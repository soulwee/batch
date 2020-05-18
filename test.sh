#!/bin/bash

CLASSNAME=com.demo.ApplicationApp
PROJECTDIR=$PWD
CLASSPATH=
function libjars(){
for file in $PROJECTDIR/www/WEB-INF/lib/*
	do
	if [ -f $file ]
	then
	 #echo $file   
	 CLASSPATH="$CLASSPATH$file:"
	fi
	done
}

libjars
export CLASSPATH 

nohup java -Xms512m -Xmx1024m -Dproject.dir="$PROJECTDIR" -classpath $CLASSPATH $CLASSNAME > log.txt 2>&1 &

