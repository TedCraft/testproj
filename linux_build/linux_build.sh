#!/bin/sh
cd ..

bash mvnw package

cd database

if [ ! -e "TESTPROJDB.FDB" ]; then 
	bash dbRestoreLinux.sh
fi

exec $SHELL