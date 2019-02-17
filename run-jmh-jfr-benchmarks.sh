#!/usr/bin/env bash

java -version

sbt clean compile "project benchmark" "jmh:run -prof jmh.extras.JFR"
