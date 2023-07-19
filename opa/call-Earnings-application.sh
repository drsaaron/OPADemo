#! /bin/sh 

# call the OPA server with the "sample-data" request
requestData=earnings-call-application.json
curl -X POST -d "{ \"input\": $(cat $requestData) }" http://localhost:8181/v1/data/demo/earnings

