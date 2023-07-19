#! /bin/sh

eval $(grep ^imageName= runDocker.sh)
pullLatestDocker.sh -i $imageName

