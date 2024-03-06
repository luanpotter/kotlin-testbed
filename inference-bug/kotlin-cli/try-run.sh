#!/bin/bash

rm -rf out
mkdir out

cd out

kotlinc ../$1 -include-runtime -d a.jar
kotlin a.jar
rm a.jar

cd ../
rm -rf out
