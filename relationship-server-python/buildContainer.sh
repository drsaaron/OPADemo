#! /bin/sh

imageName=relationshipserver-python
imageVersion=0.0.1
containerName=relationshipServer

docker stop $containerName
docker rm $containerName

pip3 freeze > requirements.txt
~/scripts/buildDocker.sh -v $imageVersion -n $imageName -f

docker run -d --name $containerName --network opademo -p 25001:25001 $imageName:$imageVersion
