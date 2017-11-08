#!/usr/bin/env bash

export VERSION=1.0.3
export REGISTRY=localhost:5000/chirper

sbt "-DbuildVersion=$VERSION" -DbuildTarget=kubernetes clean docker:publishLocal

docker tag "chirper/friend-impl:$VERSION" "$REGISTRY/friend-impl:$VERSION"
docker tag "chirper/activity-stream-impl:$VERSION" "$REGISTRY/activity-stream-impl:$VERSION"
docker tag "chirper/front-end:$VERSION" "$REGISTRY/front-end:$VERSION"
docker tag "chirper/chirp-impl:$VERSION" "$REGISTRY/chirp-impl:$VERSION"
docker push "$REGISTRY/friend-impl:$VERSION"
docker push "$REGISTRY/activity-stream-impl:$VERSION"
docker push "$REGISTRY/front-end:$VERSION"
docker push "$REGISTRY/chirp-impl:$VERSION"

docker tag "chirper/friend-impl:$VERSION" "$REGISTRY/friend-impl:latest"
docker tag "chirper/activity-stream-impl:$VERSION" "$REGISTRY/activity-stream-impl:latest"
docker tag "chirper/front-end:$VERSION" "$REGISTRY/front-end:latest"
docker tag "chirper/chirp-impl:$VERSION" "$REGISTRY/chirp-impl:latest"
docker push "$REGISTRY/friend-impl:latest"
docker push "$REGISTRY/activity-stream-impl:latest"
docker push "$REGISTRY/front-end:latest"
docker push "$REGISTRY/chirp-impl:latest"