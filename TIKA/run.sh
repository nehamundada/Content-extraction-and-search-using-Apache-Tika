#!/usr/bin/bash

rm -r bin/*

javac -cp ./src:lib/opencsv-3.0.jar:lib/tika-app-1.6.jar:lib/java-json.jar:lib/simmetrics_jar_v1_6_2_d07_02_07.jar:lib/ -d bin/ src/edu/usc/Main.java

java -cp ./bin:lib/opencsv-3.0.jar:lib/tika-app-1.6.jar:lib/java-json.jar:lib/simmetrics_jar_v1_6_2_d07_02_07.jar:lib/ edu.usc.Main
