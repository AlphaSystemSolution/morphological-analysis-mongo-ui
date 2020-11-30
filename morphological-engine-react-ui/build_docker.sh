#!/usr/bin/env bash

rm -r build

npm run build:docker

docker images -a | grep "morphological-engine-ui" | awk '{print $3}' | xargs docker rmi

docker build -t sfali23/morphological-engine-ui .

docker push sfali23/morphological-engine-ui
