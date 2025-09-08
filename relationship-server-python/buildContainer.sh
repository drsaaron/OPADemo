#! /bin/sh

imageName=drsaaron/relationshipserver-python
imageVersion=0.0.1
containerName=relationshipServer

docker stop $containerName
docker rm $containerName

pip3 freeze | grep -v ansible | grep -v pydantic > requirements.txt
~/shell/buildDocker.sh -v $imageVersion -n $imageName -f

docker run -d --name $containerName --network host -p 25001:25001 $imageName:$imageVersion
