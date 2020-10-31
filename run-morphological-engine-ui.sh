#!/usr/bin/env bash

set -e

java -Darabic.font.name="Al Bayan" -Darabic.regular.font.size=16 -Darabic.heading.font.size=24\
 -jar morphological-engine-ui/build/libs/morphological-engine-ui-4.0.0-SNAPSHOT.jar
