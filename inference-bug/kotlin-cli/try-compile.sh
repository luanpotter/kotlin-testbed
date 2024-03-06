#!/bin/bash

rm -rf out

mkdir out
kotlinc $1 -d out/out.jar

rm -rf out
