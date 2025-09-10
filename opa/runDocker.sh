#! /bin/sh 

ipfilter=netmask
buildfor="remote"
filtercomm=tail

rego=earnings.rego

imageName=openpolicyagent/opa:latest-envoy-rootless
containerName=opa

docker stop $containerName
docker rm $containerName

# get the localhost IP, for use in API calls
echo "building for $buildfor"
ip=$(ifconfig |grep inet | grep $ipfilter | $filtercomm -1 | awk '{ print $2 }')

# run the docker image with the desired rego.  We can run with all regos in this directory (just put the directory name), but that seems to require
# docker to restart each time we run, which isn't good for this demo purpose
docker run -d --name $containerName --network opademo -v `pwd`:/work/data -p 8181:8181 $imageName run --server --addr :8181 /work/data/$rego

