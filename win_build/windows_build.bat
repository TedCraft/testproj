@echo off

cd ..

call mvnw package

cd database

if not exist TESTPROJDB.FDB call dbRestore.bat

pause