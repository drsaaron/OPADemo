#! /bin/sh

# call for all failures as an application, indicated by not having a JWT
echo "calling for all failures...."
curl http://localhost:25000/failures 2>/dev/null | jq

# get a specific failure as an application
echo
echo "calling for failure on 1001..."
curl  http://localhost:25000/failures/1001 2>/dev/null | jq


echo
