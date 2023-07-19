#! /bin/sh 

ipfilter=netmask
buildfor="remote"
filtercomm=tail

while getopts :hr OPTION
do
    case $OPTION in
	h)
	    homeoffice=y
	    ipfilter=broadcast
	    buildfor="home office"
	    filtercomm=head
	    ;;
	r)
	    remote=y
	    ;;
	*)
	    echo "invalid option $OPTARG" 1>&2
	    exit 1
    esac
done

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
docker run -d --name $containerName --add-host relationshipServer:$ip -v `pwd`:/work/data -p 8181:8181 $imageName run --server --addr :8181 /work/data/$rego

